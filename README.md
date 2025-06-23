# Finora

Finora is a personal finance management Android app that helps users track income, expenses, and manage their finances with a modern UI, dark/light theme support, and Firebase backend integration.

## Features
- User registration, login, and logout (with Firebase Authentication)
- Add, view, and search income and expenses
- Dashboard with charts and analytics (theme-aware)
- Profile management and password reset
- Dark/Light theme toggle
- Income tax and EMI calculator
- Feedback and About sections

## Tech Stack
- Java (Android)
- Firebase Authentication, Realtime Database, Storage
- MPAndroidChart for charts
- Material Components for UI

## Setup Instructions
1. **Clone the repository:**
   ```sh
   git clone https://github.com/Chirag-S-Kotian/Finora.git
   cd genz
   ```
2. **Open in Android Studio.**
3. **Configure Firebase:**
   - Download your `google-services.json` from Firebase Console and place it in `app/`.
   - Ensure your Firebase project has Authentication (Email/Password), Realtime Database, and Storage enabled.
    - Add the necessary dependencies in `build.gradle` files:
      ```groovy
      // Project-level build.gradle
      buildscript {
            dependencies {
                 classpath 'com.google.gms:google-services:4.3.10' // Check for latest version
            }
      }
    
      // App-level build.gradle
      dependencies {
            implementation 'com.google.firebase:firebase-auth:21.0.1' // Check for latest version
            implementation 'com.google.firebase:firebase-database:20.0.3' // Check for latest version
            implementation 'com.google.firebase:firebase-storage:20.0.0' // Check for latest version
            implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0' // Check for latest version
      }
      apply plugin: 'com.google.gms.google-services'
      ```
        - Ensure you have the Google Services plugin applied at the bottom of your `app/build.gradle` file:
        ```groovy
        apply plugin: 'com.google.gms.google-services'
        ```
        - Sync your project with Gradle files.
4. **Build the project:**
   - Sync Gradle and build the app.
5. **Run on an emulator or device.**

## API Details
### Authentication
- **Register:**
  - Endpoint: Firebase Auth (Email/Password)
  - Fields: Email, Password
  - Validation: Email format, password length
- **Login:**
  - Endpoint: Firebase Auth (Email/Password)
  - Fields: Email, Password
  - Validation: Email format, password length
- **Logout:**
  - Firebase signOut

### Database Structure (Firebase Realtime Database)
- `/IncomeData/{uid}/` — Stores user income entries
- `/ExpenseData/{uid}/` — Stores user expense entries
- Each entry contains: amount, type, note, date, etc.

#### Example Income Entry
```json
{
  "IncomeData": {
    "uid123": {
      "-Mxyz...": {
        "amount": 5000,
        "type": "Salary",
        "note": "June Salary",
        "date": "2025-06-01"
      }
    }
  }
}
```


### Storage
- Used for profile images and file uploads (Firebase Storage)

## Usage
- Register or login to your account
- Add income/expense entries
- View dashboard for analytics
- Use the theme toggle in the toolbar
- Search and filter your transactions
- Access calculators and profile from the navigation menu

## Security
- Sensitive files like `google-services.json` are excluded from git tracking
- All user data is stored securely in Firebase

## Contributing
Pull requests are welcome. For major changes, please open an issue first.

## License
[Apache 2.0](LICENSE)
