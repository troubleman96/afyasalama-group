# Dynamic Water Intake Tracking

The Water Intake Tracking system in Afya Salama is designed to provide personalized hydration goals that adapt to your environment and activity level.

## 💧 Core Functionality

### 1. Daily Progress Tracking
- Users can log water intake in increments of **250ml** or **500ml**.
- Current intake is persisted in a local SQLite database (`water_intake` table).
- Progress is reset daily (automatically filtered by the current date).

### 2. Dynamic Goal Calculation
The "Smart Goal" is calculated using the `WaterGoalManager` class based on the following formula:
- **Base Goal**: 2000 ml.
- **Weather Factor**: +50 ml for every 1°C above 25°C.
- **Activity Factor**: +100 ml for every 1000 steps taken.

## 🛠 Technical Implementation

### Sensors & APIs
- **Step Counter (`Sensor.TYPE_STEP_COUNTER`)**: The app uses the hardware step counter sensor to track physical activity throughout the day.
- **Weather Integration**: (Mocked) The system is architected to receive temperature data from weather services to influence hydration needs.

### Data Layer
The `DatabaseHelper` manages the `water_intake` table:
- `id`: Primary key.
- `amount`: Intake amount in milliliters.
- `timestamp`: Time of entry.

## 🔐 Permissions
To function correctly, this feature requires:
- **Physical Activity (`ACTIVITY_RECOGNITION`)**: Required to access step counter data on Android 10+.
- **Internet**: Required for fetching real-time weather data.

## 🚀 How to Test
1. Open the **Water Intake** screen from the Dashboard.
2. Grant **Physical Activity** permission if prompted.
3. Add water using the buttons and observe the progress bar.
4. Walk with the phone to see the **Steps** count increase and the **Goal** adjust automatically.
