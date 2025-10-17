# Train Ticket Booking System

A command-line Java application for booking train tickets with features for user management, train search, and ticket booking.

## Features

- **User Management**
  - User Registration (Sign Up)
  - User Authentication (Login)
  - Secure Password Handling with BCrypt

- **Train Operations**
  - Search Trains by Source and Destination
  - View Train Schedules and Routes
  - Real-time Seat Availability Check

- **Ticket Management**
  - Book Train Tickets
  - View Booking History
  - Cancel Bookings
  - Seat Selection

## Technology Stack

- Java 8
- Gradle for Build Management
- Jackson for JSON Processing
- JBCrypt for Password Hashing
- JUnit for Testing

## Project Structure

```
TicketBookingApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   └── java/
│   │   │       └── org/example/
│   │   │           ├── App.java
│   │   │           ├── entities/
│   │   │           ├── services/
│   │   │           └── utils/
│   │   └── test/
│   └── build.gradle
└── gradle/
```

## Getting Started

### Prerequisites

- Java 8 or higher
- Gradle 7.0 or higher

### Installation

1. Clone the repository
```bash
git clone [repository-url]
cd TicketBookingApp
```

2. Build the project
```bash
./gradlew build
```

3. Run the application
```bash
./gradlew run
```

## Usage

1. **Start the Application**
   - Run the application using gradle
   - Select from the available menu options

2. **Sign Up/Login**
   - Create a new account or login with existing credentials
   - Passwords are securely hashed using BCrypt

3. **Search Trains**
   - Enter source and destination stations
   - View available trains and their schedules

4. **Book Tickets**
   - Select a train from search results
   - Choose available seats from the seat map
   - Confirm booking

5. **Manage Bookings**
   - View booking history
   - Cancel existing bookings

## Data Storage

The application uses JSON files for data persistence:
- `users.json`: Stores user information and booking history
- `trains.json`: Stores train and seat information

## Contributing

Feel free to fork the repository and submit pull requests. For major changes, please open an issue first to discuss the proposed changes.

## License

This project is licensed under the MIT License - see the LICENSE file for details.