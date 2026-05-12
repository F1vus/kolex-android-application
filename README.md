# 🎫 KOLEX - Mobile Train Ticket System

KOLEX is a modern Android application designed for seamless train ticket searching, booking, and management. Built as an educational project, it demonstrates a full-stack mobile experience with complex business logic, real-time API integration, and user-centric design.

---

## ✨ Key Features

### 🔐 Authentication & Security
- **Identity Management**: Secure Login and Registration system.
- **JWT Integration**: Token-based authentication using `AuthInterceptor` and `TokenManager`.
- **Session Persistence**: Automatic login state restoration.

### 👥 User & Profile Management
- **Multi-Profile Support**: Manage multiple traveler profiles (Family, Friends) under one account.
- **CRUD Operations**: Create, update, and delete sub-profiles with ease.
- **Virtual Wallet**: Track balance, view transaction history, and top-up funds.

### 🔍 Travel Search & Discovery
- **Connection Engine**: Search for train routes between stations with date/time filters.
- **Route Details**: View comprehensive travel plans, including stops, durations, and distances.
- **Live Status**: Real-time handling of "No Routes Found" or network connectivity issues.

### 🛒 Booking & Payments
- **Seat Selection**: 
  - **Graphical Mode**: Interactive wagon maps for precise seat selection.
  - **Random Mode**: Quick booking for users on the go.
- **Reservation Timer**: 10-minute hold on selected seats to ensure fair booking.
- **Flexible Payments**: Instant ticket purchase using the internal balance system.

### 🎫 Ticket Management
- **Digital Wallet**: Access all your tickets in a clean, organized list.
- **Ticket Details**: View QR codes (ready for integration), station info, and passenger data.
- **Refund Policy**: Automated refund system returning 15% of the ticket price to the user balance.
- **PDF Export**: Generate and share professional PDF versions of your tickets.

---

## 🏗️ Architecture

The project follows **Clean Architecture** principles using the **MVVM (Model-View-ViewModel)** pattern:

- **View (Fragments/Activities)**: Responsible for UI rendering and user interactions using View Binding.
- **ViewModel**: Manages UI-related data and business logic, surviving configuration changes via LiveData.
- **Repository**: Acts as a mediator between different data sources (API and Cache).
- **Model**: POJO classes with Lombok for clean data representation.
- **Remote Data Source**: Retrofit interface defining RESTful endpoints.

---

## 🛠️ Technical Stack

- **Language**: Java / Kotlin
- **Networking**: Retrofit 2, OkHttp 3, Gson
- **Architecture**: MVVM, Repository Pattern, Singleton
- **UI Components**: ConstraintLayout, ViewPager2, RecyclerView, Material Design 3
- **Jetpack**: LiveData, ViewModel, Lifecycle, Navigation Component
- **Utilities**: 
  - `PdfShareUtil`: PDF generation and sharing.
  - `LocalDateTimeAdapter`: Custom GSON serialization for Java 8 dates.
  - `AuthInterceptor`: Automatic header injection for API calls.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Jellyfish or newer.
- Android SDK 26+ (Android 8.0).
- Backend server running (default: `http://172.20.10.4:8080/`).

### Setup
1. **Initialize API**: 
   Update `ApiClient.java` with your server's IP address.
2. **Mock Mode**: 
   The app includes a robust mock system. Toggle `USE_MOCK_DATA` in repositories (`UserRepository`, `TicketRepository`, etc.) to develop without a live backend.
3. **Build**: 
   ```bash
   ./gradlew assembleDebug
   ```

---

## 📖 Documentation Reference

| Document | Description |
|----------|-------------|
| [Architecture Diagrams](ARCHITECTURE_DIAGRAMS.md) | Visual guides on data flow and state management. |
| [Integration Guide](INTEGRATION_GUIDE.md) | Step-by-step instructions for backend developers. |
| [Mock Data Guide](MOCK_DATA_GUIDE.md) | How to use and extend the offline testing system. |
| [Tickets Implementation](TICKETS_IMPLEMENTATION_COMPLETE.md) | Deep dive into the ticket rendering system. |

---

## 🎓 Project Status
Developed as part of the "Mobile Systems Project" at the University. 

**Status**: ✅ Version 1.0 (Production Ready)
