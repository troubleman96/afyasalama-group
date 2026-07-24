# Afya Salama - App Module

This module contains the source code for the Afya Salama Android application.

## Authentication Flow
The app starts with a **Welcome Screen** that introduces the app's purpose.
- **Get Started**: Leads to the Login Screen.
- **Login Screen**: Allows users to enter credentials or navigate to registration.
- **Register Screen**: Allows new users to create an account.

## Implementation Details
- **Activities**:
    - `WelcomeActivity`: Entry point.
    - `LoginActivity`: Handles user login.
    - `RegisterActivity`: Handles new user registration.
- **Layouts**:
    - `activity_welcome.xml`: Clean landing page layout.
    - `activity_login.xml`: Input-focused login layout.
    - `activity_register.xml`: Form-based registration layout.

## Medication Reminder System
The core feature of Afya Salama is helping users manage their medications.
- **Dashboard**: Displays a list of all scheduled medications.
- **Add Medication**: Users can input the medication name, dosage, and set a reminder time using a time picker.
- **SQLite Database**: Medications are stored locally on the device.
- **Local Notifications**: The app schedules alarms that trigger high-priority notifications at the set time.

## Implementation Details
- **Models**: `Medication` class for data representation.
- **Database**: `DatabaseHelper` manages SQLite CRUD operations.
- **UI Components**:
    - `DashboardActivity`: Uses a `RecyclerView` with `MedicationAdapter`.
    - `AddMedicationActivity`: Form for data entry.
- **Alert Logic**:
    - `AlarmHelper`: Schedules alarms using `AlarmManager`.
    - `AlarmReceiver`: Handles the alarm broadcast and displays the notification.

## Next Steps
- Integrate Accelerometer for the shake-to-stop feature.
- Implement Water Tracking functionality.
- Integrate Weather API for dynamic hydration goals.
