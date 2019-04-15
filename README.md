# TransDataCollection

## About
This android app is an effort to collect data to be used for translation from the lay people with the most minimal efforts. The backed for this project resides at[TransDataCollectionBackend](https://github.com/cgnetswara/TransDataCollectionBackend). The app is totally bound on the backend and hence installation of it is necessary, head over to the provided link to do the same.

## Installation Instructions
Please follow the instructions carefully for a successful installation process.

* Import project from Android Studio. To download Android Studio, visit [this](https://developer.android.com/studio).

* Install the Django Backend App from [here](https://github.com/cgnetswara/TransDataCollectionBackend).

* Change the `BASE_URL` variable in `app/src/main/java/com/example/myapplication/MainActivity.java` if you are using a network address. Network address is required if you are running it on your device, in which case make sure your system (running the [django app](https://github.com/cgnetswara/TransDataCollectionBackend)) and the device is connected on the same network. If you are using an emulator, there is no need to switch to `BASE_URL`.

* Run the app. 

* **Note: The application as of now crashes after 10 questions, this will be fixed soon.**

## Team and Contributors
* Anurag Shukla (Android App) (IIIT Naya Raipur)
* Ankush Jain (Backend) (IIIT Naya Raipur)
* Devansh Mehta (Testing and Brain Storming) (CGNet Swara)
* Vipin Kirar (Field Testing) (CGNet Swara)
* Sebastin Santy (App Testing) (Microsoft Research)
