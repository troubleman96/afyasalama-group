# Afya Salama App

Afya Salama is a smart mobile application designed to help users manage their medication schedules and daily hydration levels. The app focuses on ensuring users never miss a dose through an interactive and reliable reminder system.

## 🚀 Features
- **Medication Reminders**: Add medication names, dosages, and exact reminder times.
- **Interactive Alarm**: Uses the phone's **accelerometer sensor**. Users must shake the phone to stop the alarm, ensuring they are fully awake.
- **Modern UI**: Clean, Figma-inspired design with capsule-shaped inputs and blue accents.
- **Water Intake Tracking**: (Planned) Dynamic goals based on weather and activity.
- **Health Log**: Track water consumption and medication history.

## 🛠 Tech Stack
- **Language**: Java
- **Database**: SQLite (via `DatabaseHelper`)
- **Architecture**: Classic Android with Helper/Receiver patterns.
- **Sensors**: Accelerometer (Shake Detection).
- **UI Framework**: XML with Material 3 components.

## 🔔 Medication Reminder System

The reminder system is built for high reliability on modern Android versions (Android 12 to Android 14+).

### Permissions & Configuration
To ensure reminders work correctly, the app utilizes the following:
1.  **Notification Permission (`POST_NOTIFICATIONS`)**: Required for Android 13+ to display the medication alert.
2.  **Exact Alarm Permission (`SCHEDULE_EXACT_ALARM`)**: Required for Android 12+ to fire the alarm precisely at the scheduled time. The app will prompt you to enable this in system settings.
3.  **Full Screen Intent (`USE_FULL_SCREEN_INTENT`)**: Allows the alarm screen to appear automatically even when the phone is locked.
4.  **Wake Lock & Vibrate**: Ensures the phone stays awake during the alarm and provides tactile feedback.

### How it Works
1.  **Scheduling**: When a medication is saved, `AlarmHelper` calculates the next occurrence and schedules it using `AlarmManager.setExactAndAllowWhileIdle`.
2.  **Triggering**: The `AlarmReceiver` catches the alarm intent, creates a high-priority notification channel, and launches the `AlarmActivity`.
3.  **Interaction**: `AlarmActivity` plays the default alarm sound and vibrates. The user must **shake the device** (detected via `ShakeDetector`) to dismiss the alarm and mark the medication as taken.

## 🎨 UI Design
The interface has been meticulously reconstructed to match the Figma designs provided in the `Afya salama - designs/` folder.
- **Capsule Shapes**: All input fields and buttons use a modern capsule (fully rounded) style for a friendly and accessible look.
- **Color Palette**:
    - `Sky Blue (#AEE6FF)`: Used for the welcome screen header.
    - `Dark Blue (#00478F)`: Used for primary actions and buttons.
    - `Light Grey (#F1F1F1)`: Used for input field backgrounds.

## 🏁 Getting Started
1.  **Connect Device**: Connect your Android phone via USB and enable USB Debugging.
2.  **Build**: Open the project in Android Studio and run the `:app` module.
3.  **Permissions**: On the first launch, click "Get Started" and grant the requested Notification and Exact Alarm permissions.
4.  **Set Reminder**: Go to the Dashboard, click the `+` button, add a medication, and set a time 1 minute into the future to test the system!
