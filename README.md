# Værsmart

*Contributors:*

Developed by andrklae, christft, hananam, viktorol, muntahad and shanzae.

Azure access provided by UiO

Weather data provided by MET Norway

## Overview

### Documentation

The detailed information regarding the architecture and modeling of the project can be found in
their respective [ARCHITECTURE](ARCHITECTURE.md) and [MODELING](MODELING.md) markdown files.

### Requirements

Environment:

- Android Device running Android 9 API level 28 or newer

Connectivity:

- To use the user's location, the user must enable location data on their device, and they
  will be asked to give permissions for the app to use their location. The user will be asked to
  enable their location if they do not already have it enabled.
- The app needs an internet connection to update the stored weather data from the previous session

## Running the app

The project can be downloaded or cloned from
our [GitHub Repository](https://github.uio.no/IN2000-V24/team-13)

By instructions from Sondre (sjefsgruppelærer), the API-keys are written in clear text in the code,
and not included in the gitignore. We ask for your discretion when handling this code and testing
the app, as these API-keys are paid and costs money each time they are used. You may of course test
the app as much as you please, but we ask that you do not share the source code nor the git
repository.

To run the app;

1. Unpack if it's a .zip file
2. Open the project in Android Studio
3. Select a simulator or device to run the app on
4. Click run 'app' or run unitTests

## Dependencies, libraries and repositories

The project is created with:

### *Backend resources*

- Android Core KTX 1.12.0
- Junit 4.13.2
- Google Dagger Hilt 2.49

### *UI tools*

- Google Maps Android SDK 5.0.0
- Coil Compose 2.5.0
- Coil SVG 2.1.0
- Airbnb Lottie 5.2.0

#### Jetpack Compose UI App Development Toolkit

- Compose Material3 1.2.1
- Compose Animation 1.6.6

### *API & Data*

- Ktor Client 2.3.8
- Azure OpenAI 1.0.0-beta.3
- Google Play Services Location 21.2.0
- Google Play Services Maps 18.2.0

### *Repositories for dependency libraries*

- Maven Central Repository
- Google Repository