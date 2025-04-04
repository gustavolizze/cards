# 💳 Card Management Project

This is a multi-module **Gradle** project for managing credit/debit card data via a simple REST API. The system supports creating individual cards, searching, and uploading batches via file upload.

---

## 🧰 Prerequisites

Before running the project, make sure you have the following installed:

- ✅ **Java 21.0.5**
- 🐳 **Docker Compose** installed and the Docker daemon running
- 💡 **IntelliJ IDEA** (recommended for development)

---

## 🚀 Running the Project

1. **Start Docker Compose**  
   Make sure Docker is running. The services defined in the `docker-compose.yml` file will start automatically when the application runs.

2. **Open the Project in IntelliJ IDEA**

3. **Run the Application**  
   Locate the `App.java` file inside the `web` module and run it as a Java application.

   The backend will start on:
    http://localhost:8080



---

## 🧪 API Usage

### 📌 Create Card

Create a new card with the following `POST` request:

```bash
curl --request POST \
--url http://localhost:8080/cards \
--header 'Content-Type: application/json' \
--header 'User-Agent: insomnia/10.3.1' \
--data '{
 "number": "5555341244441115",
 "name": "Gustavo Lizze",
 "month": 8,
 "year": 2030,
 "securityCode": "123"
}'
```

### 🔍 Search Cards

Search for cards using the number parameter (optional):

```bash
curl --request GET \
  --url 'http://localhost:8080/cards?number=555' \
  --header 'User-Agent: insomnia/11.0.2'

```

### 📂 Batch Card Creation

Upload a file containing card data (see file format below):



```bash
curl --request POST \
  --url http://localhost:8080/cards/batch \
  --header 'Content-Type: multipart/form-data' \
  --header 'User-Agent: insomnia/11.0.2' \
  --form file=@batch.txt

```

⚠️ The batch.txt file must be placed in the root directory of the project.
Example:
[batch.txt](batch.txt)

### 🏗️ Project Structure

This is a multi-module Gradle project. The main application logic is split into different modules for better organization and scalability:

web/ – Contains the entry point (App.java) and REST controllers.

core/ – Business logic and domain services.

database/ – External system integrations (e.g., mysql).

docker-compose.yml – Automatically starts required services for the app.


### 📎 Notes

The Docker services required by the application (e.g., databases or other dependencies) will be started automatically via the included docker-compose.yml.

No additional setup is needed beyond starting the application.