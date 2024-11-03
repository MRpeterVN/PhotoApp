# CameraX Image Capture App
## Tính năng

- **Chụp ảnh**: Sử dụng CameraX để chụp ảnh.
- **Chỉnh sửa ảnh**: Cắt và chỉnh sửa ảnh.
- **Quản lý dữ liệu ảnh**: Sử dụng Room để lưu trữ và quản lý các dữ liệu liên quan đến ảnh trong cơ sở dữ liệu SQLite của ứng dụng.
- **Tải và xử lý ảnh**: Tích hợp Glide và Coil để hiển thị ảnh từ bộ nhớ trong một cách mượt mà và tối ưu.


### Cài đặt thư viện

Thêm các dòng sau vào file `build.gradle` của module:

```groovy
dependencies {
    // CameraX
    val cameraxVersion = "1.3.0"
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")

    // Guava
    implementation("com.google.guava:guava:31.0.1-android")

    // Room components
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Coil
    implementation("io.coil-kt:coil:2.4.0")

    // uCrop
    implementation("com.github.yalantis:ucrop:2.2.8")
}

## Video Demo



![z5995303104405_346fa60f93de1754b0775d930c1b1c14](https://github.com/user-attachments/assets/517a31a9-1df0-4ed8-ab6e-b0670644b6ca)
![z5995303104421_5907ad8913247457cb2b1f957270192a](https://github.com/user-attachments/assets/45222369-f2e7-4cc9-8553-8d75ffb9eac2)
![z5995303104365_d3d9780f128d1faeaea5f59c4ea1e6bf](https://github.com/user-attachments/assets/4e5caa8d-ce67-4878-8b09-068732cb0e5d)
