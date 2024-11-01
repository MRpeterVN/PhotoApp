# CameraX Image Capture App

Ứng dụng này được xây dựng bằng Kotlin, tích hợp các thư viện CameraX, Room, Glide, Coil và uCrop.

## Tính năng

- **Chụp ảnh**: Sử dụng CameraX để chụp ảnh.
- **Chỉnh sửa ảnh**: Cắt và chỉnh sửa ảnh với tỷ lệ tùy chọn trước khi lưu.
- **Quản lý dữ liệu ảnh**: Sử dụng Room để lưu trữ và quản lý các dữ liệu .
- **Tải và xử lý ảnh**: Tích hợp Glide và Coil để hiển thị ảnh từ bộ nhớ .
## Thư viện được sử dụng

### 1. CameraX
```kotlin
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
![Screenshot 2024-11-01 154152](https://github.com/user-attachments/assets/c97dbb66-a6c7-41ae-aacb-472015afb3b3)
![Screenshot 2024-11-01 154515](https://github.com/user-attachments/assets/35d7e8c2-4c87-4ff4-96ae-0f359beab316)
