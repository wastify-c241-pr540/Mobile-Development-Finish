<img src="https://github.com/wastify-c241-pr540/Mobile-Development-Finish/assets/95511540/88c3fa77-ea39-4a45-97c6-69c6af81c301" width="100" height="100" align="right" />

# Wastify
Capstone Project Bangkit 2024
![Wastify App](https://github.com/wastify-c241-pr540/Mobile-Development-Finish/assets/95511540/cf13e4da-3c52-4857-968d-d33ff55a6f36)


## About Our App

Wastify is an innovative educational information system application designed to enhance awareness and understanding of both organic and non-organic waste. Leveraging advanced digital image processing and image classification technologies, Wastify can accurately identify different types of waste. This cutting-edge capability not only educates users about proper waste segregation but also promotes environmental responsibility.

## How To Make This Android App Project

### How To Build This Project

* Set BASE_URL variable
```
BASE_URL = https://cloud-computing-2-5td7pos36q-et.a.run.app/
```

### Libraries We Use

| Library name  | Usages        | Dependency    |
| ------------- | ------------- | ------------- |
| [Retrofit2](https://square.github.io/retrofit/) | Request API and convert json response into an object | implementation "com.squareup.retrofit2:retrofit:2.9.0" <br> implementation "com.squareup.retrofit2:converter-gson:2.9.0" |
| [Tflite](https://www.tensorflow.org/lite) | Machine learning process | implementation "org.tensorflow:tensorflow-lite-task-audio:0.3.0" |
| [Navigation component](https://developer.android.com/guide/navigation)| Navigation between pages | implementation "androidx.navigation:navigation-fragment-ktx:2.4.2" <br> implementation "androidx.navigation:navigation-ui-ktx:2.4.2" |
| [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle?hl=id) | Connecting frontend and backend | implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1" <br> implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.4.1" |
|[Firebase](https://firebase.google.com/docs/android/setup)|For authentication purposes|implementation "com.google.firebase:firebase-auth:23.0.0" <br> implementation"com.google.firebase:firebase-inappmessaging:21.0.0"|
|[Glide](https://github.com/bumptech/glide)|For image loading and caching library|implementation "com.github.bumptech.glide:glide:4.16.0"|
|[Camera](https://github.com/bumptech/glide)|Image taking for real-time detection| implementation "androidx.camera:camera-core:1.3.4" <br> implementation "androidx.camera:camera-camera2:1.3.4" <br>     implementation "androidx.camera:camera-view:1.3.4" <br> implementation "androidx.camera:camera-lifecycle:1.3.4"|
