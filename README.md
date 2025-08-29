# Hold - Secure Note-Taking App

Hold is a secure note-taking application for Android, built with modern Android development practices and a focus on simplicity and security.

## Core Features

*   **Create, View, and Manage Notes**: Easily jot down and organize your thoughts.
*   **Secure Storage**: Notes are stored securely using SQLCipher for database encryption, ensuring your private information stays private.
*   **Clean User Interface**: Built with Jetpack Compose for a modern, reactive, and intuitive user experience.

## Tech Stack & Libraries

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Architecture**: MVVM (Model-View-ViewModel)
*   **Database**: [Room Persistence Library](https://developer.android.com/jetpack/androidx/releases/room) with [SQLCipher](https://www.zetetic.net/sqlcipher/) for encryption
*   **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
*   **Navigation**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation)
*   **Asynchronous Programming**: Kotlin Coroutines

## Project Structure

The project follows a standard Android app structure:

```
Hold/
└── app/
    ├── schemas/                  # Room database schema export location
    └── src/
        └── main/
            ├── java/com/example/hold/
            │   ├── data/         # Room DB (Entities, DAO, Database class)
            │   ├── di/           # Hilt Dependency Injection modules (AppModule, CryptoManager, NoteRepository)
            │   ├── ui/           # Jetpack Compose UI
            │   │   ├── navigation/ # Navigation graph and routes (e.g., AppNavigation.kt)
            │   │   ├── noteedit/   # Screen for editing a note (Composable + ViewModel)
            │   │   ├── noteslist/  # Screen for listing notes (Composable + ViewModel)
            │   │   └── theme/      # App theme (Color.kt, Theme.kt, Type.kt)
            │   ├── DefaultActivity.kt # Launcher Activity
            │   ├── MainActivity.kt    # Another Activity (if used)
            │   └── MainApplication.kt # Application class for Hilt setup
            ├── res/                  # Android resources (layouts, drawables, values, etc.)
            └── AndroidManifest.xml   # App manifest file
```

## Getting Started

1.  **Clone the repository**:
    ```bash
    git clone <your-repository-url>
    ```
2.  **Open in Android Studio**:
    *   Open the latest stable version of Android Studio.
    *   Select "Open an Existing Project" and navigate to the cloned `Hold` directory.
3.  **Gradle Sync**:
    *   Allow Android Studio to sync the Gradle files and download all necessary dependencies.
4.  **Build and Run**:
    *   Select a target emulator or connect a physical Android device.
    *   Click the "Run" button (or Shift+F10).

## Notes

*   The application uses SQLCipher for on-device database encryption. The pre-built AAR for SQLCipher typically handles NDK dependencies.
*   Room database schemas are exported to the `app/schemas/` directory, which is useful for database migrations.

---

This README provides a good overview for anyone looking at your project.
