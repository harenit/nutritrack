# 🥗 NutriTrack — AI-Powered Meal Tracker

A full-stack Spring Boot + SQLite web app for tracking meals, BMI, calories, and meal memories with AI suggestions.

---

## 🚀 How to Run

### Prerequisites
- **Java 17+** must be installed ([Download here](https://adoptium.net/))
- **Maven 3.8+** must be installed ([Download here](https://maven.apache.org/download.cgi))

### Steps

```bash
# 1. Open this folder in terminal / VS Code terminal

# 2. Run the app
mvn spring-boot:run

# OR if using Maven wrapper (Linux/Mac):
chmod +x mvnw
./mvnw spring-boot:run

# OR on Windows:
mvnw.cmd spring-boot:run
```

### 3. Open your browser
```
http://localhost:8081
```

---

## 📁 Project Structure

```
nutritrack/
├── src/
│   └── main/
│       ├── java/com/nutritrack/
│       │   ├── NutriTrackApplication.java   ← Main entry point
│       │   ├── config/
│       │   │   ├── DataInitializer.java     ← Seeds food data into DB
│       │   │   └── WebConfig.java           ← Static file serving
│       │   ├── controller/
│       │   │   ├── AuthController.java      ← Login / Register / Logout
│       │   │   ├── MealController.java      ← Meal REST API
│       │   │   ├── PageController.java      ← HTML page routing
│       │   │   └── ProfileController.java   ← Profile update API
│       │   ├── model/
│       │   │   ├── User.java               ← User entity
│       │   │   ├── MealEntry.java          ← Meal log entity
│       │   │   └── FoodItem.java           ← Food item entity
│       │   ├── repository/                  ← JPA repositories
│       │   └── service/
│       │       ├── GeminiService.java       ← AI suggestions
│       │       ├── MealService.java         ← Meal logic
│       │       └── UserService.java         ← User logic
│       └── resources/
│           ├── application.properties       ← Config (port, DB, API key)
│           └── static/
│               ├── css/style.css           ← Global styles
│               ├── js/utils.js             ← Shared JS utilities
│               ├── login.html
│               ├── register.html
│               ├── dashboard.html          ← Main meal logging page
│               ├── weekly.html             ← Weekly charts/analysis
│               ├── calendar.html           ← Meal calendar & memories
│               ├── community.html          ← Shared meal posts
│               ├── bmi.html               ← BMI calculator
│               └── profile.html           ← Account management
├── data/                                   ← Auto-created: SQLite DB + uploads
├── pom.xml                                 ← Maven dependencies
└── README.md
```

---

## 🔑 Pages & Features

| URL | Description |
|-----|-------------|
| `/login` | Login with username & password |
| `/register` | Register with full health profile |
| `/dashboard` | Log meals, see today's calories + AI tips |
| `/weekly` | Weekly calorie chart (Chart.js) |
| `/calendar` | Click any date to see meals; add photos & captions |
| `/community` | See shared meal posts from all users |
| `/bmi` | BMI calculator with health tips |
| `/profile` | Update profile, change password, delete account |

---

## 🤖 AI Suggestions

- Uses **Google Gemini API** to suggest healthy alternatives for unhealthy foods
- The API key is already in `application.properties`
- Triggered automatically when you log an unhealthy item (pizza, burger, etc.)

---

## 🗄️ Database

- Uses **SQLite** — no setup needed, file created automatically at `./data/nutritrack.db`
- Food items (healthy & unhealthy) are seeded on first run
- All user data is stored per-user

---

## ⚙️ Configuration

Edit `src/main/resources/application.properties`:

```properties
server.port=8081                    # Change port here
app.gemini.api-key=YOUR_KEY_HERE    # Replace with your Gemini API key
app.upload.dir=./data/uploads       # Where meal photos are stored
```

---

## 🍎 Supported Food Items

**Healthy:** apple, banana, milk, egg, salad, rice, chapati, dal, vegetables, oats, idli, dosa, curd

**Unhealthy:** pizza, burger, fries, chips, chocolate, cake, icecream, soda, fried rice, biryani, noodles

> You can add more items directly to the SQLite DB or extend `DataInitializer.java`
