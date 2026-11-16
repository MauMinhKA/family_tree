# 🌳 Family Tree (Gia Phả Số)

<p align="center">
  <img src="app/src/main/res/drawable/app_icon_archbase_1794158028332.jpg" alt="Family Tree Logo" width="120" height="120" style="border-radius: 24px;" />
</p>

<p align="center">
  <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-2.0%2B-purple?style=flat-square&logo=kotlin" alt="Kotlin"></a>
  <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Jetpack%20Compose-UI-blue?style=flat-square&logo=jetpackcompose" alt="Jetpack Compose"></a>
  <a href="https://m3.material.io/"><img src="https://img.shields.io/badge/Material%203-Design-orange?style=flat-square&logo=materialdesign" alt="Material 3"></a>
  <a href="https://supabase.com"><img src="https://img.shields.io/badge/Supabase-Backend-green?style=flat-square&logo=supabase" alt="Supabase"></a>
  <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square" alt="License: MIT"></a>
  <a href="https://developer.android.com"><img src="https://img.shields.io/badge/Platform-Android-brightgreen?style=flat-square&logo=android" alt="Android"></a>
</p>

---

## 📖 Giới thiệu dự án

**Family Tree** là ứng dụng di động hiện đại được phát triển trên nền tảng Android, giúp các gia đình, dòng họ lưu giữ, quản lý và kết nối các thế hệ thành viên một cách trực quan, khoa học và bảo mật. Dự án giải quyết bài toán số hóa gia phả truyền thống bằng công nghệ đám mây, giúp con cháu dù ở bất cứ đâu cũng có thể tra cứu cội nguồn, xem mối quan hệ huyết thống và cập nhật thông tin sự kiện của dòng họ.

---

## 🛠️ Công nghệ sử dụng (Tech Stack)

- **Ngôn ngữ:** [Kotlin](https://kotlinlang.org/) (100% Modern Kotlin)
- **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) & [Material 3](https://m3.material.io/)
- **Kiến trúc:** Clean Architecture (Data, Domain, Presentation) kết hợp mô hình **MVI (Model - View - Intent)**
- **Xử lý bất đồng bộ:** Kotlin Coroutines & Flow
- **Điều hướng:** Jetpack Navigation Compose
- **Backend & Cơ sở dữ liệu:** [Supabase](https://supabase.com/) (Authentication, PostgreSQL Database)
- **Lưu trữ cục bộ:** Jetpack DataStore Preferences
- **Mạng & Phân tích JSON:** Retrofit, OkHttp, Moshi (với KSP Codegen)
- **Hiệu ứng & Đồ họa:** Lottie Compose, Coil (Load ảnh bất đồng bộ)
- **Kiểm thử (Testing):** JUnit, Robolectric, Coroutines Test, Compose UI Test

---

## ✨ Các tính năng nổi bật

1. **🔐 Xác thực & Quản lý phiên đăng nhập:**
   - Đăng nhập/Đăng ký tài khoản an toàn thông qua Supabase Auth.
   - Màn hình Splash thông minh kiểm tra trạng thái xác thực tự động, chuyển hướng mượt mà đến trang chủ hoặc màn hình đăng nhập.
2. **🌳 Trực quan hóa Cây gia phả:**
   - Hiển thị cấu trúc gia phả nhiều thế hệ trực quan, sinh động kết hợp hiệu ứng Lottie (`tree_animation.json`).
   - Tra cứu chi tiết thông tin cá nhân, tiểu sử, ngày giỗ, ngày sinh và mối quan hệ cha mẹ - con cái - vợ chồng.
3. **👥 Quản lý thành viên dòng họ:**
   - Thêm mới, chỉnh sửa hoặc cập nhật thông tin thành viên trong gia tộc.
   - Phân quyền và bảo mật thông tin dòng họ trên cơ sở dữ liệu đám mây PostgreSQL.
4. **☁️ Đồng bộ hóa đám mây thời gian thực:**
   - Dữ liệu được lưu trữ trực tuyến trên Supabase, đảm bảo luôn cập nhật mới nhất cho tất cả các thành viên trong gia đình cùng truy cập.
5. **🎨 Giao diện hiện đại (Material 3):**
   - Thiết kế chuẩn Material Design 3 hỗ trợ linh hoạt Dark Mode và Light Mode, mang lại trải nghiệm thị giác tuyệt vời.

---

## 📂 Cấu trúc thư mục & Kiến trúc

Dự án áp dụng mô hình **Clean Architecture** kết hợp **MVI** nhằm tách biệt rõ ràng các tầng logic, dễ bảo trì và kiểm thử:

```text
com.example/
│
├── [core/](Family_tree/app/src/main/java/com/example/core/)                       # Các thành phần chung dùng toàn cục
│   ├── di/                     # Dependency Injection (AppContainer thủ công)
│   ├── [mvi/](Family_tree/app/src/main/java/com/example/core/mvi/)                    # Khung MVI cơ bản (MviIntent, MviState, MviSideEffect, MviViewModel)
│   ├── [navigation/](Family_tree/app/src/main/java/com/example/core/navigation/)             # Cấu hình điều hướng (AppNavHost, ScreenRoute)
│   └── [result/](Family_tree/app/src/main/java/com/example/core/result/)                 # Xử lý kết quả trả về & Lỗi mạng (AppResult, NetworkError)
│
├── [data/](Family_tree/app/src/main/java/com/example/data/)                       # Tầng dữ liệu (Data Layer)
│   ├── [local/](Family_tree/app/src/main/java/com/example/data/local/)                  # Lưu trữ cục bộ (DataStore Preferences)
│   ├── [remote/](Family_tree/app/src/main/java/com/example/data/remote/)                 # Giao tiếp mạng & Supabase (SupabaseClient, DTOs)
│   └── [repository/](Family_tree/app/src/main/java/com/example/domain/repository/)             # Triển khai Repository interfaces
│
├── [domain/](Family_tree/app/src/main/java/com/example/domain/)                     # Tầng nghiệp vụ (Domain Layer - Pure Kotlin)
│   ├── [model/](Family_tree/app/src/main/java/com/example/domain/model/)                  # Các Domain Models cốt lõi
│   ├── [repository/](Family_tree/app/src/main/java/com/example/domain/repository/)             # Repository interfaces
│   └── [usecase/](Family_tree/app/src/main/java/com/example/domain/usecase/)                # Các Business Use Cases (CheckAuthStatus, GetExampleItems, v.v.)
│
├── [presentation/](Family_tree/app/src/test/java/com/example/presentation/)               # Tầng giao diện (Presentation Layer - Jetpack Compose & MVI)
│   ├── [home/](Family_tree/app/src/main/java/com/example/presentation/home/)                   # Màn hình Trang chủ (HomeScreen, HomeViewModel, HomeContract)
│   ├── [login/](Family_tree/app/src/main/java/com/example/presentation/login/)                  # Màn hình Đăng nhập (LoginScreen)
│   └── [splash/](Family_tree/app/src/test/java/com/example/presentation/splash/)                 # Màn hình Khởi động (SplashScreen, SplashViewModel, SplashContract)
│
├── ui/                         # Giao diện chung & Theme
│   └── [theme/](Family_tree/app/src/main/java/com/example/ui/theme/)                  # Material 3 Theme (Color, Type, Theme)
│
├── [ArchBaseApplication.kt](Family_tree/app/src/main/java/com/example/ArchBaseApplication.kt)      # Android Application Class
└── [MainActivity.kt](Family_tree/app/src/main/java/com/example/MainActivity.kt)             # Activity chính chứa NavHost