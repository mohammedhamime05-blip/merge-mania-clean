# Merge Mania - Reconstructed Game Project

This project contains the reconstructed, clean, and fully functioning source code of your Merge Mania game. 

## Features
- **Clean Gameplay:** Grid-based merge mechanics, levels 1-10 dragon items, 10 stage selection levels, high score saving.
- **Removed Adware:** The malicious clickjacking purchase intercepts and automated background tasks have been completely removed.
- **Safe Cross-Promotions:** Re-implemented a clean config loader (`AdManager`) that safely connects to a web JSON to fetch game recommendations.

---

## How to Set Up & Host Your Config File (GitHub)

1. Upload the provided **[config.json](file:///c:/Users/pc/Desktop/POPPS%20HM/config.json)** file in the root of this project to a **GitHub Gist** or a public GitHub repository.
2. Click the **"Raw"** button on GitHub to get the direct raw link of your JSON file. It should look like:
   `https://gist.githubusercontent.com/username/gist_id/raw/config.json`
3. Open **[MainActivity.java](file:///c:/Users/pc/Desktop/POPPS%20HM/app/src/main/java/com/Arctic/Parade/Game/Merge/MainActivity.java)** and replace the `CONFIG_JSON_URL` constant (line 12) with your raw URL:
   ```java
   private static final String CONFIG_JSON_URL = "https://gist.githubusercontent.com/your-username/your-gist-id/raw/";
   ```
4. Now, you can control the `isActive` state and games list dynamically by editing your JSON file on GitHub!

---

## How to Build the Project

1. Open **Android Studio**.
2. Select **File > Open** and choose the folder **`c:\Users\pc\Desktop\POPPS HM`**.
3. Android Studio will automatically import the project and sync the Gradle files.
4. Click **Run** (`Shift + F10`) to launch the game on an emulator or connected device.
