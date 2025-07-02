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

<p align="center">
  <img src="https://img.shields.io/badge/build-passing-brightgreen" alt="Build Status">
  <img src="https://img.shields.io/badge/platform-Android-blue" alt="Platform">
  <img src="https://img.shields.io/badge/license-MIT-lightgrey" alt="License">
  <img src="https://img.shields.io/badge/Firebase-integrated-yellow" alt="Firebase">
  <img src="https://img.shields.io/badge/Charts-MPAndroidChart-orange" alt="MPAndroidChart">
  <img src="https://img.shields.io/badge/IDE-Android%20Studio-green" alt="Android Studio">
  <img src="https://img.shields.io/badge/Open%20Source-Yes-success" alt="Open Source">
</p>

<h1 align="center">CashKeeper</h1>
<p align="center"><b>Modern Personal Finance Manager for Android</b></p>

---
## 🌟 Overview
CashKeeper is a modern, feature-rich personal finance management app for Android. Track your income, expenses, and analyze your financial trends with a beautiful UI and real-time Firebase integration. Built for speed, clarity, and privacy.
---
## 🚀 Features
- **User Authentication:** Secure login, registration, and password reset (Firebase Auth)
- **Income & Expense Tracking:** Add, edit, view, and search transactions
- **Advanced Analytics:**
  - Bar charts for income and expenses (real-time, filterable)
  - Filter by month, year, or custom date range 📆
- **Dashboard:** Daily/monthly expense summaries
- **Profile Management:** View & update profile, upload image 👥
- **Export:** Export all data as CSV 📁
- **Modern UI:** Material Components, custom fonts (Montserrat, Roboto), responsive layouts
- **Navigation:** Bottom navigation bar and navigation drawer
- **Robust Error Handling:** Detailed error messages for all auth and network failures 🚨
- **Firebase-First:** All data stored securely in Firebase Realtime Database and Storage 🔒


## 📷 Screenshots
<!-- Add screenshots here -->
- Dashboard
- Analytics
- Add/Edit Expense
- Profile
- About


## 🗂️ Project Structure

```
app/src/main/java/com/example/genz/
├── AnalyticsFragment.java      # Analytics page, income/expense bar charts
├── DashboardFragment.java      # Dashboard with expense summaries
├── ExpenseFragment.java        # Add/view/edit/delete expenses
├── IncomeFragment.java         # Add/view/edit/delete income
├── Profile.java                # User profile page
├── Registration.java           # User registration
├── MainActivity.java           # App entry, navigation
├── about.java                  # About page
├── change_password.java        # Change password screen
├── first_home_page.java        # Main navigation activity
├── home_screen.java            # Home page
├── Model/Data.java             # Transaction data model
└── ... (other utility/activity files)

app/src/main/res/values/
app/src/main/res/layout/
├── activity_about.xml              # About page layout
├── fragment_analytics.xml          # Analytics screen layout
├── fragment_dashboard.xml          # Dashboard layout
├── fragment_expense.xml            # Expenses page layout
├── fragment_income.xml             # Income page layout
├── profile_toolbar.xml, about_toolbar.xml, ...
└── ... (other layouts)

app/src/main/res/menu/
├── bottommenu.xml                  # Bottom navigation menu
├── nav_menu.xml                    # Navigation drawer menu
└── ...
```


## 🔗 Firebase Data Structure
- `/ExpenseData/{uid}/` — All expense entries for user
- `/IncomeData/{uid}/` — All income entries for user
- Each entry: `{ amount, type, note, id, date }`
- `/ProfileImages/{uid}/` — Profile images


## 📊 Analytics & Dashboard
- **Dashboard:**
  - Bar chart of expenses (toggle daily/monthly)
  - Quick summary of income, expense, and balance
- **Analytics Page:**
  - Bar charts: Income and Expense (real-time, filterable)
  - Filter: All time, this month, this year, custom range


## ⚡ Setup & Usage
1. **Clone the repository:**
   ```sh
   git clone https://github.com/Chirag-S-Kotian/CashKeeper.git
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

## 🛠️ Tech Stack
- **Language:** Java
- **UI:** Android XML, Material Components, CardView
- **Charts:** MPAndroidChart (BarChart)
- **Backend:** Firebase Realtime Database, Firebase Auth, Firebase Storage
- **IDE:** Android Studio Chipmunk 2021.2.1
- **Fonts:** Montserrat, Roboto (downloadable fonts)

---

## 📄 License
MIT License (see LICENSE file)

---

© 2025 Chirag S Kotian. CashKeeper is open-source and community-driven.

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

# CashKeeper
A modern personal finance management app for Android, built with Firebase and MPAndroidChart.


This project, CashKeeper, is licensed under the [Apache 2.0](LICENSE) license.
This README provides a comprehensive overview of the CashKeeper app, its features, setup instructions, and project structure. It is designed to help developers and users understand the app's functionality and how to contribute or use it effectively.