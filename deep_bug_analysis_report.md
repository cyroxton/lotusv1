prompt_name: "Analyse Approfondie des Bugs Lotus : Crash Langue & Refresh Musique"
description: "Rapport détaillé des tests, analyses et solutions pour les bugs critiques de Lotus."

résumé_tests:
  - test_langue:
      description: "Test du changement de langue via LocalePicker"
      résultat: "Échec - Crash de l'application"
      logs: "Erreur: Unresolved reference 'context' dans LanguageManager.kt ligne 19"
  
  - test_refresh_musique:
      description: "Test de l'affichage des musiques après scan"
      résultat: "Succès partiel - Implémentation réactive en place mais non optimale"
      logs: "Aucune erreur visible, mais l'UI ne se met pas à jour automatiquement"

hypothèses_validées:
  - bug_langue:
      - "Le paramètre 'context' dans LanguageManager n'est pas stocké comme propriété de classe"
      - "La méthode recreate() est correctement appelée mais sur une référence invalide"
      - "onSaveInstanceState est implémenté mais ne sauvegarde pas la langue actuelle"
  
  - bug_refresh_musique:
      - "TrackRepository est déjà réactif avec Flow<List<Track>>"
      - "TrackRepositoryImpl implémente un cache invalidable avec MutableStateFlow"
      - "MusicScanner appelle bien invalidateCache() après les scans"
      - "PlayerViewModel collecte correctement le Flow mais la mise à jour UI peut être améliorée"

hypothèses_invalidées:
  - bug_langue:
      - "Incompatibilité avec ComponentActivity" - Non confirmé
      - "Perte de références d'objets lors de la recréation" - Problème plus spécifique
  
  - bug_refresh_musique:
      - "Absence de mécanisme réactif" - Déjà implémenté
      - "MusicScanner ne notifie pas TrackRepository" - Notification en place

logs_erreur:
  - bug_langue: |
      Erreur de compilation dans LanguageManager.kt:19
      Unresolved reference: context
      Cause: Le paramètre 'context' est utilisé dans le setter de 'language' mais n'est pas stocké comme propriété
  
  - bug_refresh_musique: |
      Aucune erreur explicite, mais analyse des logs montre:
      - Flow émis correctement dans TrackRepositoryImpl
      - Collecté dans PlayerViewModel
      - Problème potentiel dans la chaîne de mise à jour UI

recommandations:
  - bug_langue:
      priorité: "Haute"
      solutions:
        - "Modifier LanguageManager.kt pour stocker 'context' comme propriété privée de classe"
        - "Améliorer onSaveInstanceState dans MainActivity.kt pour sauvegarder la langue actuelle"
        - "Restaurer la langue dans onCreate avec savedInstanceState"
      code_suggéré: |
        // Dans LanguageManager.kt
        class LanguageManager(private val context: Context) {
            // Le reste du code reste inchangé car 'context' est maintenant une propriété
        }
        
        // Dans MainActivity.kt
        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putString("language", languageManager.language)
        }
        
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // ...
            savedInstanceState?.getString("language")?.let { languageManager.language = it }
        }
  
  - bug_refresh_musique:
      priorité: "Moyenne"
      solutions:
        - "Optimiser la collecte du Flow dans les composants UI"
        - "Ajouter des logs de débogage pour tracer les émissions et collections"
        - "Vérifier que LaunchedEffect dans MainScreen.kt collecte correctement le Flow"
      code_suggéré: |
        // Dans MainScreen.kt
        LaunchedEffect(Unit) {
            viewModel.tracks.collect { tracks ->
                Log.d("MainScreen", "Tracks mis à jour: ${tracks.size}")
                // Assurez-vous que cette collection déclenche un recomposite
            }
        }
        
        // Dans PlayerViewModel.kt - Vérifier cette partie
        val tracks = trackRepository.getTracks().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

conclusion: |
  L'analyse approfondie révèle que les deux bugs ont des causes bien identifiées:
  
  1. Le crash au changement de langue est dû à une erreur de portée de variable dans LanguageManager.kt où 'context' n'est pas stocké comme propriété de classe.
  
  2. Le problème de rafraîchissement des musiques est plus subtil - l'architecture réactive est en place (Flow, StateFlow, invalidation de cache), mais la chaîne de mise à jour UI peut être optimisée.
  
  Les corrections proposées sont ciblées et minimales, se concentrant sur les problèmes spécifiques sans modifier l'architecture globale de l'application.