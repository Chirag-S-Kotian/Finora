<!-- Badges -->
<p align="center">
  <a href="#"><img src="https://img.shields.io/badge/build-passing-brightgreen" alt="Build Status"></a>
  <a href="#"><img src="https://img.shields.io/badge/platform-Android-blue" alt="Platform"></a>
  <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/license-MIT-lightgrey" alt="License"></a>
  <a href="https://firebase.google.com/"><img src="https://img.shields.io/badge/Firebase-integrated-yellow" alt="Firebase"></a>
  <a href="https://github.com/PhilJay/MPAndroidChart"><img src="https://img.shields.io/badge/Charts-MPAndroidChart-orange" alt="MPAndroidChart"></a>
  <a href="https://developer.android.com/studio"><img src="https://img.shields.io/badge/IDE-Android%20Studio-green" alt="Android Studio"></a>
  <a href="#"><img src="https://img.shields.io/badge/Open%20Source-Yes-success" alt="Open Source"></a>
</p>
<p align="center">
  <!-- Play Store badge placeholder -->
  <a href="#"><img src="https://img.shields.io/badge/Coming%20Soon-Google%20Play-black?logo=google-play" alt="Google Play"></a>
</p>

# Finora: CashKeeper

A modern, feature-rich personal finance management app for Android (2025)

---

Finora (formerly CashKeeper) helps users track income, expenses, and financial trends with powerful analytics, a beautiful UI, and seamless Firebase integration.

## 🚀 Features
- **User Authentication:** Secure login, registration, and password reset (Firebase Auth)
- **Income & Expense Tracking:** Add, edit, view, and search transactions
- **Advanced Analytics:**
  - Pie chart for category breakdown
  - Line charts for spending and income trends
  - Combined income vs expense analytics
  - Filter by month, year, or custom date range
- **Dashboard:** Daily/monthly toggle for expense chart
- **Profile Management:** View/update profile, upload image
- **Calculators:** Income Tax and EMI calculators
- **Export:** Export all data as CSV
- **Modern UI:** Material Components, custom fonts (Montserrat, Roboto), responsive layouts
- **Navigation:** Bottom navigation bar and navigation drawer
- **Feedback & About:** Built-in feedback form and about page
- **Robust Error Handling:** Detailed error messages for all auth and network failures
- **Firebase-First:** All data stored securely in Firebase Realtime Database and Storage

## 📱 Screenshots
*Coming soon: add screenshots of Dashboard, Analytics, Profile, and Calculators*

## 🗂️ Project Structure
- `app/src/main/java/com/example/genz/` — Main app code
  - `DashboardFragment.java` — Dashboard with daily/monthly expense chart
  - `AnalyticsFragment.java` — Advanced analytics (income, expense, combined charts)
  - `ExpenseFragment.java` & `IncomeFragment.java` — CRUD for transactions
  - `Model/Data.java` — Transaction data model
  - `emi.java` — EMI calculator
  - `first_home_page.java` — Main activity with navigation
  - ...and more (profile, feedback, etc.)
- `app/src/main/res/layout/` — UI layouts (XML)
- `app/src/main/res/values/` — Colors, strings, arrays, styles
- `app/src/main/res/drawable/` — Icons and vector assets

## 🔗 Firebase Data Structure
- **Authentication:** Email/password
- **Database:**
  - `/ExpenseData/{uid}/` — All expense entries for user
  - `/IncomeData/{uid}/` — All income entries for user
  - Each entry: `{ amount, type, note, id, date }`
- **Storage:**
  - `/ProfileImages/{uid}/` — Profile images

## 📊 Analytics & Dashboard
- **Dashboard:**
  - Bar chart of expenses (toggle daily/monthly)
  - Quick summary of income, expense, and balance
- **Analytics Page:**
  - Pie chart: Expense by category
  - Line chart: Expense trend
  - Line chart: Income trend
  - Combined chart: Income vs Expense
  - Filter: All time, this month, this year, custom range

## 🛠️ Setup & Usage
1. **Clone the repository:**
   ```sh
   git clone https://github.com/Chirag-S-Kotian/Finora.git
   cd genz
   ```
2. **Open in Android Studio** (latest recommended)
3. **Add Firebase:**
   - Download `google-services.json` from your Firebase Console
   - Place it in `app/`
   - Enable Auth, Realtime Database, and Storage in your Firebase project
   - Add dependencies in `build.gradle`:
     ```groovy
     implementation 'com.google.firebase:firebase-auth:21.0.1'
     implementation 'com.google.firebase:firebase-database:20.0.3'
     implementation 'com.google.firebase:firebase-storage:20.0.0'
     implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
     ```
   - Apply Google Services plugin at the bottom:
     ```groovy
     apply plugin: 'com.google.gms.google-services'
     ```
4. **Fonts/Colors:**
   - Uses downloadable fonts (no setup needed)
   - Colors in `res/values/colors.xml`
5. **Build & Run:** Sync Gradle, build, and run on device/emulator
6. **Sign in or register to use all features**

## 🧑‍💻 Contributing
- Fork this repo and submit pull requests!
- Please open issues for bugs or feature requests.
- Follow best practices for Java/Android and Firebase security.

## ⚠️ Troubleshooting
- If you see resource or R class errors, try `Build > Clean Project` and `Sync Project with Gradle Files` in Android Studio.
- Make sure `google-services.json` is in the correct location.
- Check Firebase rules for database and storage access.
- If charts don’t show, ensure MPAndroidChart is in your dependencies.

## 📄 License
MIT License (see LICENSE file)

---

© 2025 Chirag S Kotian. Finora is open-source and community-driven.

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
### Profile Management
- User profile data is stored under `/UserProfile/{uid}/`
- Contains fields like name, email, profile image URL, etc.

### Feedback
- User feedback is stored under `/Feedback/{uid}/`
- Contains fields like feedback text, timestamp, etc.
### Storage
- Used for profile images and file uploads (Firebase Storage)
- Images are stored under `/ProfileImages/{uid}/`

### Analytics
- Dashboard analytics are generated using data from the Realtime Database

# Finora: CashKeeper
A modern personal finance management app for Android, built with Firebase and MPAndroidChart.

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