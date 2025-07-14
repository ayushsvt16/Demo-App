
# ROV Demo App

This is a native Android application developed in Java for controlling a wireless ROV (Remotely Operated Vehicle) over a local Wi-Fi network using WebSocket communication. The app interfaces with an ESP32-powered system, enabling real-time movement commands.

## Features

- Connects to ESP32 via WebSocket using IP and port
- Provides simple directional controls: Forward, Backward, Left, Right, Stop
- Input fields to set WebSocket server IP address and port
- Reconnect and disconnect buttons for manual control of the connection
- Works over hotspot/local Wi-Fi network
- Simple responsive UI for low-latency feedback

## Screenshots

> **Note:** Screenshots are excluded intentionally per user request.

## Requirements

- Android Studio (Giraffe or later recommended)
- Android SDK 33+
- ESP32 WebSocket server (running and accessible over Wi-Fi)
- Java 8+

## Setup Instructions

1. **Clone the repository**  
   ```bash
   git clone https://github.com/ayushsvt16/Demo-App.git
   ```

2. **Open the project**  
   Open the project in Android Studio. Gradle will sync automatically.

3. **Run the App**  
   - Connect your Android device or use an emulator.
   - Click **Run** or use `Shift + F10`.

4. **Connect to ESP32**  
   - Make sure the ESP32 and the Android device are connected to the same Wi-Fi network (or the ESP is connected to the Android device’s hotspot).
   - Enter the IP address and port (e.g., `192.168.4.1:5000`) and click `Connect`.

## ESP32 WebSocket Setup

> The app expects a running WebSocket server on the ESP32.

Refer to your ESP32 firmware (Arduino or PlatformIO) with something like:

```cpp
webSocket.begin("192.168.4.1", 5000, "/");
webSocket.onEvent(webSocketEvent);
```

### Commands Sent from App:

| Button    | Sent Message |
|-----------|--------------|
| Forward   | `forward`    |
| Backward  | `backward`   |
| Left      | `left`       |
| Right     | `right`      |
| Up        | `up`         |
| Down      | `down`       |
| Stop      | `stop`       |

## Local IP & Port Notes

- Typically, when hosting from PC Hotspot:
  - IP: `192.168.137.1` (or find via `ipconfig`)
  - Port: Match with ESP32 WebSocket server (e.g., `5000`)

## How to Check Ports and Firewall on Windows (for ESP32 Connection)

### Check Open Ports

```cmd
netstat -a | find "5000"
```

### Allow Port in Firewall

1. Open **Windows Defender Firewall** → Advanced Settings
2. Click **Inbound Rules** → **New Rule**
3. Choose **Port** → TCP → Enter port (e.g., 5000)
4. Allow connection → Choose all profiles → Name the rule (e.g., `ESP32 WebSocket`)

## Libraries Used

- Native Android (Java)
- [OkHttp WebSocket](https://square.github.io/okhttp/)
- No external UI frameworks — lightweight for embedded control

## File Structure (Key Parts)

```
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/demoapp/
│   │   │   │   ├── MainActivity.kt
│   │   │   ├── res/layout/
│   │   │   │   ├── activity_main.xml
│   │   │   └── AndroidManifest.xml
```

## Future Improvements

- Video streaming preview from ESP32-CAM
- Sensor data visualization
- Dark mode toggle

---

**Made with ❤️ by [Ayush Srivastava](https://github.com/ayushsvt16)**
