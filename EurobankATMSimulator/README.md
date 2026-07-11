# Eurobank ATM Simulator — Android App

This is a ready-to-build Android Studio project. It wraps the exact same
ATM simulator (the HTML file you've been testing) inside a native
Android WebView, so it installs and runs as a normal app icon on a
phone or tablet — full screen, no browser bar.

## Easiest way to get an actual .apk file (no software install, ~5 min)

This project includes a GitHub Actions workflow
(`.github/workflows/build.yml`) that builds the APK for you automatically
in the cloud, for free:

1. Create a free account at github.com if you don't have one.
2. Create a new (public or private) repository, e.g. `eurobank-atm-app`.
3. Upload the **contents** of this unzipped folder to that repository
   (drag-and-drop on the GitHub web page works, or `git push` if you're
   comfortable with git).
4. Go to the **Actions** tab of the repository — a workflow called
   "Build APK" will run automatically (takes ~2-3 minutes).
5. When it finishes (green checkmark), click into that run, scroll to
   **Artifacts**, and download `eurobank-atm-simulator-debug-apk` — it's
   a zip containing `app-debug.apk`.
6. Transfer `app-debug.apk` to your Android phone (email it to yourself,
   Google Drive, USB, etc.), open it on the phone, and allow
   "install from unknown sources" if asked. That installs the app.

This produces a real, installable `.apk` — no Android Studio required.

## Alternative: build it yourself in Android Studio (5 minutes)

1. Install **Android Studio** (free, from developer.android.com) if you
   don't have it already.
2. Unzip this project folder.
3. In Android Studio: **File → Open** → select the unzipped
   `EurobankATMSimulator` folder.
4. Let Gradle sync (Android Studio will offer to generate the Gradle
   wrapper automatically — accept it, or click "Sync Now" if prompted).
5. Plug in an Android phone (with USB debugging on) or start an
   emulator, then click the green **Run ▶** button.

That's it — the app installs and opens straight into the ATM simulator.

## What's inside

- `app/src/main/assets/index.html` — the full simulator (identical to
  the file you've been previewing), loaded locally on the device.
- `app/src/main/java/.../MainActivity.java` — a thin WebView wrapper
  with JavaScript and DOM storage enabled, and the Android back button
  mapped to in-page navigation.
- Standard Gradle project files (`build.gradle`, `settings.gradle`,
  manifest, resources).

## Notes

- The app needs internet access on the device the first time it loads
  a screen, because the page pulls Tailwind CSS, FontAwesome icons,
  and Google Fonts from their CDNs (same as in the browser preview).
  If you want a fully offline app, those three `<script>`/`<link>` tags
  in `index.html` would need to be swapped for locally bundled copies —
  say the word and I can do that next.
- Text-to-speech (the "Άκουσε ξανά την οδηγία" button and the guided
  voice prompts) uses the same Web Speech API as the browser version.
  It works out of the box on nearly all modern Android WebViews, but
  voice quality/availability depends on the TTS engine installed on
  the device.
- Package name is `gr.eurobank.atmsimulator` — change it in
  `app/build.gradle` (`applicationId`) and the manifest package if you
  want something different before publishing anywhere.
