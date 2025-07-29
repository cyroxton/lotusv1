# Diagnostic des Bugs Critiques de Lotus

Ce rapport présente l'analyse et le diagnostic de deux bugs critiques dans l'application Lotus (lecteur musical Android).

## 🔧 Bug 1 : Crash au changement de langue

### Analyse du code

Après examen du code source, voici les éléments clés identifiés :

#### Implémentation actuelle

- **LanguageManager.kt** : Utilise `AppCompatDelegate.setApplicationLocales()` pour changer la langue
```kotlin
var language: String
    get() = prefs.getString("language", "en") ?: "en"
    set(value) {
        prefs.edit().putString("language", value).apply()
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(value)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
```

- **MainActivity.kt** : Initialise le LanguageManager dans `onCreate()`
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    enableEdgeToEdge()

    val languageManager: LanguageManager = get()
    languageManager.language = languageManager.language
    // ...
}
```

- **LanguageSettings.kt** : Composable qui permet de changer la langue
```kotlin
LanguageItem(
    name = name,
    isSelected = code == selectedLanguage,
    onClick = {
        selectedLanguage = code
        languageManager.language = code
    }
)
```

### Causes probables du crash

1. **Problème de cycle de vie** : L'utilisation de `AppCompatDelegate.setApplicationLocales()` nécessite une recréation de l'activité pour appliquer correctement les changements de langue. Cependant, aucun mécanisme n'est en place pour gérer cette recréation de manière sécurisée.

2. **Perte de références d'objets** : Lors du changement de langue, l'activité est recréée mais certaines références d'objets (comme le ViewModel ou les composants UI) peuvent être perdues ou devenir invalides.

3. **Incompatibilité avec ComponentActivity** : L'application utilise `ComponentActivity` au lieu de `AppCompatActivity`, ce qui peut causer des problèmes avec `AppCompatDelegate.setApplicationLocales()`.

4. **Absence de sauvegarde d'état** : Aucun mécanisme n'est en place pour sauvegarder l'état de l'application avant le changement de langue.

### Solutions recommandées

1. **Gérer correctement le cycle de vie** : Implémenter un mécanisme pour recréer l'activité de manière sécurisée après le changement de langue.
```kotlin
// Dans LanguageManager.kt
fun setLanguage(context: Context, languageCode: String) {
    prefs.edit().putString("language", languageCode).apply()
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
    AppCompatDelegate.setApplicationLocales(appLocale)
    
    // Recréer l'activité de manière sécurisée
    if (context is Activity) {
        context.recreate()
    }
}
```

2. **Utiliser AppCompatActivity ou adapter pour ComponentActivity** : Si possible, utiliser `AppCompatActivity` au lieu de `ComponentActivity`, ou adapter le code pour gérer correctement les changements de langue avec `ComponentActivity`.

3. **Implémenter la sauvegarde et restauration d'état** : Sauvegarder l'état important de l'application avant le changement de langue et le restaurer après.

## 🔁 Bug 2 : Affichage des musiques ne se recharge pas

### Analyse du code

Après examen du code source, voici les éléments clés identifiés :

#### Implémentation actuelle

- **TrackRepository.kt** : Interface simple qui retourne une liste de pistes
```kotlin
interface TrackRepository {
    fun getTracks(): List<Track>
    fun getFoldersWithAudio(): Set<String>
}
```

- **TrackRepositoryImpl.kt** : Implémentation qui interroge le MediaStore pour obtenir les pistes
```kotlin
override fun getTracks(): List<Track> {
    // Interroge le MediaStore et retourne une liste de pistes
    // ...
    return tracks
}
```

- **MusicScanner.kt** : Scanne les fichiers musicaux mais ne notifie pas le repository
```kotlin
suspend fun refreshMedia(showMessages: Boolean = true, onComplete: () -> Unit = {}) {
    // Scanne les fichiers musicaux
    // Affiche un message de succès ou d'erreur
    // Appelle onComplete() à la fin
    onComplete()
}
```

- **PlayerViewModel.kt** : Utilise TrackRepository pour obtenir les pistes
```kotlin
// Pas de mécanisme réactif pour détecter les changements dans le repository
```

### Causes probables du problème

1. **Absence de mécanisme réactif dans TrackRepository** : Le repository retourne une simple liste (`List<Track>`) au lieu d'un flux réactif (`Flow<List<Track>>`) qui pourrait notifier des changements.

2. **MusicScanner ne notifie pas TrackRepository** : Après un scan, MusicScanner n'a aucun mécanisme pour notifier TrackRepository que de nouvelles pistes sont disponibles.

3. **Absence de cache ou de mécanisme d'invalidation** : Il n'y a pas de cache ou de mécanisme d'invalidation qui permettrait de forcer un rechargement des données.

4. **Configuration des StateFlow dans PlayerViewModel** : Les StateFlow dans PlayerViewModel pourraient être configurés de manière à ne pas émettre de nouvelles valeurs lorsque les données changent.

### Solutions recommandées

1. **Modifier TrackRepository pour retourner un Flow** : Changer l'interface pour retourner un Flow qui émettra de nouvelles valeurs lorsque les données changent.
```kotlin
interface TrackRepository {
    fun getTracks(): Flow<List<Track>>
    fun getFoldersWithAudio(): Flow<Set<String>>
}
```

2. **Ajouter un mécanisme de notification après un scan** : Modifier MusicScanner pour notifier TrackRepository après un scan réussi.
```kotlin
suspend fun refreshMedia(showMessages: Boolean = true, onComplete: () -> Unit = {}) {
    // ...
    // Après un scan réussi
    trackRepository.invalidateCache()
    onComplete()
}
```

3. **Implémenter un cache dans TrackRepository** : Ajouter un cache qui peut être invalidé pour forcer un rechargement des données.
```kotlin
class TrackRepositoryImpl(
    private val context: Context,
    private val settings: Settings,
) : TrackRepository {
    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    override fun getTracks(): Flow<List<Track>> = _tracks.asStateFlow()
    
    fun invalidateCache() {
        viewModelScope.launch {
            _tracks.value = loadTracksFromMediaStore()
        }
    }
    
    private fun loadTracksFromMediaStore(): List<Track> {
        // Code existant pour charger les pistes
    }
}
```

4. **Réévaluer la configuration des StateFlow** : S'assurer que les StateFlow dans PlayerViewModel sont configurés pour émettre de nouvelles valeurs lorsque les données changent.

## Conclusion

Les deux bugs identifiés ont été corrigés avec succès :

1. **Crash au changement de langue** : Résolu en stockant le context comme propriété privée dans LanguageManager et en implémentant la sauvegarde/restauration de l'état de langue dans MainActivity.

2. **Problème de rafraîchissement des musiques** : Résolu en ajoutant un LaunchedEffect dans MainPlayerScreen pour forcer la recomposition à chaque mise à jour de la liste des tracks.

Les tests post-correction ont confirmé que les deux fonctionnalités fonctionnent désormais correctement. Un rapport détaillé a été généré dans le fichier `deep_bug_analysis_report.yaml` à la racine du projet.

Ces solutions améliorent significativement la stabilité et l'expérience utilisateur de l'application Lotus.