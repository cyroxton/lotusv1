# Configuration Cursor pour Lotus

Ce dossier contient la configuration optimisée de Cursor pour le développement du projet Lotus - Lecteur de musique Android.

## 📁 Structure

```
cursor-config/
├── settings.json          # Configuration Cursor
├── keybindings.json       # Raccourcis clavier
├── tasks.json            # Tâches Gradle
├── .cursorrules          # Règles de développement
├── prompts/              # Prompts YAML modulaires
│   ├── add_repository.yaml
│   ├── add_error_handling.yaml
│   └── improve_ui.yaml
└── README.md             # Ce fichier
```

## 🚀 Installation

1. **Copier les fichiers de configuration** :
   - Copie `settings.json` vers `.vscode/settings.json`
   - Copie `keybindings.json` vers `.vscode/keybindings.json`
   - Copie `tasks.json` vers `.vscode/tasks.json`
   - Copie `.cursorrules` vers la racine du projet

2. **Installer les extensions recommandées** :
   - Kotlin Language Server
   - Android Studio Integration
   - Prettier - Code formatter

## ⌨️ Raccourcis clavier

| Raccourci | Action |
|-----------|--------|
| `Ctrl+Shift+I` | Mode Agent Cursor |
| `Ctrl+K` | Chat inline |
| `Ctrl+L` | Question rapide |
| `Ctrl+T` | Build Gradle |
| `Ctrl+Shift+T` | Tests Gradle |
| `Ctrl+Shift+R` | Installer l'app |

## 🎯 Utilisation des prompts

### Mode Agent (`Ctrl+Shift+I`)
Pour les tâches complexes comme :
- Refactoring d'architecture
- Ajout de nouvelles fonctionnalités
- Optimisation de performance

### Chat inline (`Ctrl+K`)
Pour les modifications ponctuelles :
- Correction de bugs
- Amélioration de code existant
- Ajout de tests unitaires

### Exemple d'utilisation
```bash
# Ouvrir un prompt YAML
cat cursor-config/prompts/add_repository.yaml

# Utiliser le prompt dans Cursor
Ctrl+K: "Add Repository pattern to PlaybackService following the YAML prompt"
```

## 🔧 Tâches disponibles

| Tâche | Commande | Description |
|-------|----------|-------------|
| `build` | `./gradlew build` | Compilation complète |
| `test` | `./gradlew test` | Exécution des tests |
| `run` | `./gradlew installDebug` | Installation sur appareil |
| `clean` | `./gradlew clean` | Nettoyage du build |
| `lint` | `./gradlew lint` | Analyse statique |

## 📋 Bonnes pratiques

### Commits
- Commits fréquents après chaque sous-tâche
- Messages descriptifs : `feat: add error handling`, `fix: resolve memory leak`
- Branches pour fonctionnalités : `feature/ai-error-handling`

### Tests
- Tests unitaires pour toute nouvelle logique
- Tests d'intégration pour les composants UI
- Couverture de code > 80%

### Code
- Respecter les règles dans `.cursorrules`
- Fichiers < 300 lignes
- Documentation KDoc pour les APIs publiques

## 🎨 Workflow recommandé

1. **Planification** : Utiliser les prompts YAML pour définir la tâche
2. **Développement** : Mode Agent pour l'implémentation
3. **Tests** : Ajouter les tests unitaires
4. **Validation** : Lancer `./gradlew test`
5. **Commit** : Sauvegarder les changements
6. **Itération** : Répéter si nécessaire

## 🔍 Dépannage

### Problèmes courants
- **Indexation lente** : Vérifier que le Kotlin Language Server est activé
- **Build échoue** : Lancer `./gradlew clean` puis `./gradlew build`
- **Tests échouent** : Vérifier les dépendances dans `build.gradle.kts`

### Support
- Documentation Android : https://developer.android.com/
- Jetpack Compose : https://developer.android.com/jetpack/compose
- Kotlin Coroutines : https://kotlinlang.org/docs/coroutines.html 