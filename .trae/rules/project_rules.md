# Règles Cursor pour le projet Lotus - Lecteur de musique Android

## Architecture et Patterns
- Use Kotlin coroutines for asynchronous operations
- Follow Jetpack Compose best practices for UI components
- Ensure proper Android lifecycle handling in Activities and Services
- Implement Repository pattern for data access
- Use Dependency Injection with Hilt or Koin
- Follow MVVM architecture pattern

## Code Quality
- Write unit tests with JUnit for all new logic
- Keep files under 300 lines for better maintainability
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Respect the Apache-2.0 license in all contributions

## Android Specific
- Handle permissions properly with runtime permission requests
- Use WorkManager for background tasks
- Implement proper error handling with try-catch blocks
- Use AndroidX libraries instead of support libraries
- Follow Material Design guidelines for UI components

## Performance
- Use lazy loading for large lists
- Implement proper memory management
- Use RecyclerView with DiffUtil for efficient list updates
- Optimize image loading with Glide or Coil
- Minimize network calls with caching strategies

## Security
- Never hardcode sensitive information
- Use encrypted storage for user data
- Validate all user inputs
- Follow Android security best practices

## User Experience
- Provide meaningful error messages to users
- Implement proper loading states
- Use animations for smooth transitions
- Ensure accessibility compliance
- Support both light and dark themes 