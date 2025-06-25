# CashKeeper

CashKeeper is a personal finance management Android app that helps users track income, expenses, and manage their finances with a modern UI and Firebase backend integration.
It features user authentication, income and expense tracking, a dashboard with analytics, and various financial calculators. The app is designed to be user-friendly and visually appealing, with a consistent light theme, custom fonts, and a modern color palette.


## Features
- User registration, login, and logout (with Firebase Authentication)
- Add, view, and search income and expenses
- Dashboard with charts and analytics (daily/monthly filters)
- Profile management and password reset
- Income tax and EMI calculator
- Feedback and About sections
- Fast search for transactions
- Modern splash screen with custom font (Montserrat, Roboto)
- Detailed error messages for login failures (invalid email, wrong password, network issues, etc.)
- Consistent color palette and custom fonts for all screens
- Secure password reset via email
- Profile image upload
- Export data as CSV
- Responsive design for different screen sizes

## Tech Stack
- Java (Android)
- Firebase Authentication, Realtime Database, Storage
- MPAndroidChart for charts
- Material Components for UI
- Glide for image loading


## Setup Instructions
1. **Clone the repository:**
   ```sh
   git clone https://github.com/Chirag-S-Kotian/Finora.git
   cd genz
   ```

2. **Open in Android Studio.**
    - Ensure you have the latest version of Android Studio installed.
    - Open the project by selecting the `genz` directory.
    - Make sure you have the Android SDK and necessary components installed (Android 12 or higher recommended).
    
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
    
4. **Fonts and Colors:**
   - The app uses downloadable fonts (Montserrat, Roboto). No manual font file setup is required.
   - The color palette is defined in `res/values/colors.xml` and enforced throughout the app.

5. **Build the project:**
   - Sync Gradle and build the app.
6. **Run on an emulator or device.**
7. **Sign in with your Firebase account to test the app.**

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
- **Password Reset:**
  - Endpoint: Firebase Auth (Email/Password)
  - Fields: Email
  - Action: Send password reset email

### Database Structure (Firebase Realtime Database)
- `/IncomeData/{uid}/` — Stores user income entries
- `/ExpenseData/{uid}/` — Stores user expense entries
- Each entry contains: amount, type, note, date, etc.
- `/UserProfile/{uid}/` — Stores user profile information
- `/Feedback/{uid}/` — Stores user feedback

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

#### Example Expense Entry
```json
{
  "ExpenseData": {
    "uid123": {
      "-Mxyz...": {
        "amount": 2000,
        "type": "Groceries",
        "note": "Weekly groceries",
        "date": "2025-06-02"
      }
    }
  }
}
```
### Storage
- Used for profile images and file uploads (Firebase Storage)
- Images are stored under `/ProfileImages/{uid}/`
### Analytics
- Dashboard analytics are generated using data from the Realtime Database


## Usage
- Register or login to your account
- Add income/expense entries
- View dashboard for analytics (with daily/monthly filters)
- Search and filter your transactions
- Access calculators and profile from the navigation menu

If login fails, detailed error messages will be shown for each scenario (invalid email, wrong password, no user, network error, too many attempts, etc.).


## Security
- Firebase Authentication is used for secure user management
- Data is stored securely in Firebase Realtime Database
- Sensitive files like `google-services.json` are excluded from git tracking
- All user data is stored securely in Firebase


## Contributing
Pull requests are welcome. For major changes, please open an issue first.


## License

This project, CashKeeper, is licensed under the [Apache 2.0](LICENSE) license.