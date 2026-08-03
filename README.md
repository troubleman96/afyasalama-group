# 🏥 Afya Salama - Your Personalized Health Companion

Afya Salama is a comprehensive, smart mobile application engineered to empower users in managing their medication adherence and hydration goals. Built with a focus on reliability, accessibility, and modern design, the app ensures that critical health tasks are never overlooked.

---

## 🌟 Key Features

### 1. 🔔 Intelligent Medication Reminder System
A high-reliability alarm system designed to bypass standard mobile distractions.
- **Precision Scheduling**: Uses `AlarmManager` with exact timing to ensure reminders fire precisely when needed.
- **Interactive "Shake-to-Stop"**: Leverage the phone's **accelerometer**; users must physically shake the device to dismiss alarms, ensuring cognitive alertness.
- **Lock-Screen Override**: Automatically wakes the device and launches the interaction screen, even if the phone is locked.

### 2. 💧 Dynamic Water Intake Tracking
Hydration goals that adapt to your lifestyle and environment.
- **Smart Goals**: The app dynamically adjusts your daily water target based on **ambient temperature** and **physical activity (Step Counter)**.
- **Real-time Progress**: Visual circular progress tracking with quick-log buttons (250ml / 500ml).
- **History Logs**: Persistent storage of daily intake via SQLite.

### 3. 🔍 Drug Information & Safety Search
Instant access to medical knowledge using the **OpenFDA API**.
- **Real-time Search**: Find detailed labels for thousands of medications.
- **Safety First**: Deep dives into **Side Effects**, **Dosage Instructions**, and **Indications**.
- **Modern Search UX**: Capsule-styled search interface with efficient networking via Retrofit.

### 4. 🧭 Modern Navigation
- **Bottom Navigation Bar**: Seamlessly switch between Dashboard, Water Tracker, and Drug Search.
- **Fragment-Based Architecture**: Ensures a fast, flicker-free experience.

---

## 🛠 Tech Stack & Architecture

- **Platform**: Android (Native)
- **Language**: Java
- **UI Framework**: Material Design 3 (XML)
- **Networking**: Retrofit 2.9 + Gson (for API communication)
- **Database**: SQLite (Local persistence)
- **Hardware Integration**:
    - **Accelerometer**: For shake detection.
    - **Step Counter Sensor**: For activity tracking.
- **Architecture**: Modular Fragment-based architecture with decentralized Helper/Receiver logic.

---

## 🔐 Permissions & System Configuration

To provide "real alarm" functionality and smart tracking, the app utilizes several critical permissions:

| Permission | Purpose |
| :--- | :--- |
| `POST_NOTIFICATIONS` | Required for Android 13+ to show medication alerts. |
| `SCHEDULE_EXACT_ALARM` | Critical for firing reminders at the exact second scheduled. |
| `USE_FULL_SCREEN_INTENT` | Allows the alarm screen to pop up over the lock screen automatically. |
| `ACTIVITY_RECOGNITION` | Enables the step counter for dynamic hydration goals. |
| `WAKE_LOCK` | Keeps the CPU active during the alarm transition. |
| `INTERNET` | Required for fetching real-time drug information from OpenFDA. |

---

## 🎨 UI/UX Design

The interface is meticulously aligned with the Figma "Afya Salama" design system.
- **Design Language**: Modern, clean, and medical-friendly.
- **Shapes**: Consistent use of **Capsule (fully rounded)** input fields and buttons.
- **Colors**:
    - **Primary Blue (`#00478F`)**: Used for primary calls to action.
    - **Sky Blue (`#AEE6FF`)**: Used for background accents and headers.
    - **Light Grey (`#F1F1F1`)**: Used for high-legibility input backgrounds.

---

## 🏁 Getting Started

### Prerequisites
- Android Studio Jellyfish or newer.
- An Android device (Physical device recommended for sensor testing).

### Installation
1. **Clone the Repo**:
   ```bash
   git clone git@github.com:troubleman96/afyasalama-group.git
   ```
2. **Build**:
   Open in Android Studio and run the `:app` module.
3. **Permissions**:
   Upon first launch, click **"Get Started"** on the Welcome screen. You will be prompted to grant Notification and Exact Alarm permissions. **Please grant all permissions** for the full experience.

### Testing the Alarm
1. Navigate to the Dashboard.
2. Click the `+` button.
3. Add a test medication and set the reminder for **1 minute from now**.
4. **Lock your phone**.
5. Wait for the screen to wake up automatically and start shaking!

---

## 📦 Build Artifacts
The latest debug build is available at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 🗺 Roadmap
- [ ] Integration of real-time weather API for hydration.
- [ ] User profiles and health history visualization.
- [ ] Multi-language support.
- [ ] Dark Mode optimization.
