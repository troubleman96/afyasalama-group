# Afya Salama - App Module Technical Documentation

This module contains the core logic, UI, and hardware integrations for the Afya Salama application.

## 📁 Package Structure

### `com.afyasalama`
- **`MainActivity.java`**: The host activity managing the `BottomNavigationView` and Fragment transactions.
- **`WelcomeActivity.java`**: Handles the onboarding flow and critical permission requests (Notifications, Exact Alarms, Full Screen Intents).
- **`AlarmActivity.java`**: The high-priority activity launched when a medication is due. Handles UI flags for lock-screen override and sound/vibration logic.

### `com.afyasalama.fragments`
- **`HomeFragment.java`**: Dashboard logic, displaying the medication list via a RecyclerView.
- **`WaterFragment.java`**: Manages the hydration tracker, sensor listeners, and goal updates.
- **`SearchFragment.java`**: Logic for the OpenFDA drug search interface.

### `com.afyasalama.utils`
- **`AlarmHelper.java`**: Utility for scheduling/canceling alarms with `AlarmManager`.
- **`WaterGoalManager.java`**: Pure logic class for calculating hydration targets based on temperature and steps.
- **`ShakeDetector.java`**: Implementation of `SensorEventListener` tuned specifically for alarm dismissal.

### `com.afyasalama.network`
- **`RetrofitClient.java`**: Singleton provider for the Retrofit instance.
- **`FdaApiService.java`**: Interface defining endpoints for the OpenFDA Drug Label API.

## 🛠 Hardware & Sensor Integration

### Accelerometer (Shake Detection)
The `ShakeDetector` uses the gravity-normalized G-force to detect physical movement.
- **Threshold**: `2.1F` (Tuned for responsiveness).
- **Logic**: Requires 3 distinct shakes within a 3-second window to trigger an "OnShake" event.

### Step Counter
The `WaterFragment` registers a listener for `Sensor.TYPE_STEP_COUNTER`.
- **Logic**: The sensor provides a cumulative step count. The app calculates deltas to adjust the hydration goal dynamically (+100ml per 1000 steps).

## 📡 Networking
- **API**: [OpenFDA Drug Label API](https://open.fda.gov/apis/drug/label/)
- **Endpoints**: `drug/label.json`
- **Search Query**: Uses `openfda.brand_name` for targeted results.

## 🗄 Data Persistence
Uses a `SQLiteOpenHelper` implementation (`DatabaseHelper`) with two main tables:
1. `medications`: Stores ID, Name, Dosage, and Time.
2. `water_intake`: Stores ID, Amount (ml), and Timestamp.

## 🎨 UI Components
- **Drawables**: 
    - `capsule_edit_text.xml`: Standard background for all inputs.
    - `capsule_button_blue.xml`: Primary action button styling.
- **Themes**: Material 3 base with custom color overrides for Figma alignment.
