# Rent Anything

### A Spring Boot Project for the Haaga-Helia Backend Course

## Introduction

**Rent Anything** is a web application built with Java Spring Boot that allows the admin of the app to manage a collection of rentable items, organized into categories, while users can browse the items, check their availability, and rent them. This project showcases several backend concepts, including authentication, role-based access control, file uploads, and REST API development.

This project is developed as part of the Haaga-Helia backend course and follows an MVC architecture pattern using Spring Boot, Spring Security, Spring Data JPA, and Thymeleaf for server-side rendering.

## Features

- **Admin role**: Can create, update, delete, and manage rental items and categories.
- **User role**: Can view available items and rent them if they are not currently rented by another user.
- **File upload**: Admins can upload images for each item.
- **Login and roles**: Spring Security used for authentication and role-based access control.
- **REST API**: Exposes API endpoints for managing items, categories, and user interactions.
- **External Database**: Uses MySQL/PostgreSQL as an external database.
- **Validation**: User inputs are validated for security and data integrity.
- **JUnit Testing**: Unit tests for key functionality, including item rentals and authentication.
- **Deployment**: The app is deployable on external servers like Heroku or VPS.

---

## Database Schema

The application uses a relational database (e.g., MySQL or PostgreSQL) to store data about users, roles, categories, items, and rental information. Below is a breakdown of the key database tables:

### `roles`

The `roles` table defines the roles available in the application. The system currently supports two roles:

- **User**: Can view available items and rent them.
- **Admin**: Has full control over the management of categories and items.

| Field | Type        | Description                         |
| ----- | ----------- | ----------------------------------- |
| id    | int (PK)    | Unique Role ID                      |
| title | varchar(50) | Role name (e.g., "User" or "Admin") |

### `users`

The `users` table stores all user information, including admin accounts. Each user is associated with a single role (either a "User" or an "Admin").

| Field       | Type         | Description                              |
| ----------- | ------------ | ---------------------------------------- |
| id          | int (PK)     | Unique User ID                           |
| email       | varchar(150) | User email address                       |
| first_name  | varchar(150) | User's first name                        |
| last_name   | varchar(150) | User's last name                         |
| password    | varchar(250) | Encrypted password                       |
| phone       | varchar(20)  | User's Phone number                      |
| address     | varchar(250) | User's Postal Address                    |
| city        | varchar(50)  | User's City of residence                 |
| postal_code | varchar(5)   | User's Postal Code                       |
| role_id     | int (FK)     | Foreign key to the [roles](#roles) table |

### `categories`

The `categories` table organizes the items into different rental categories (e.g., electronics, tools, furniture). Each category can contain multiple items.

| Field       | Type         | Description                   |
| ----------- | ------------ | ----------------------------- |
| id          | int (PK)     | Unique Category ID            |
| name        | varchar(100) | Category name (e.g., "Tools") |
| description | varchar(255) | Description of the category   |

### `items`

The `items` table stores information about the rental items. Items are assigned to categories and have an availability status.

| Field       | Type          | Description                                        |
| ----------- | ------------- | -------------------------------------------------- |
| id          | int (PK)      | Unique Item ID                                     |
| name        | varchar(150)  | Name of the rental item                            |
| description | text          | Detailed description of the item                   |
| price       | decimal(10,2) | Rental price per day                               |
| image_url   | varchar(255)  | URL of the item image (uploaded by admin)          |
| available   | boolean       | Whether the item is available for rent             |
| category_id | int (FK)      | Foreign key to the [categories](#categories) table |

### `rentals`

The `rentals` table keeps track of which user has rented which item and the rental period.

| Field        | Type     | Description                              |
| ------------ | -------- | ---------------------------------------- |
| id           | int (PK) | Unique Rental ID                         |
| user_id      | int (FK) | Foreign key to the [users](#users) table |
| item_id      | int (FK) | Foreign key to the [items](#items) table |
| rental_start | datetime | Start date of the rental                 |
| rental_end   | datetime | End date of the rental                   |

---

## REST API Endpoints

The application exposes a REST API for interaction with rental items and categories. Below are some of the available endpoints:

### Category Endpoints

- **GET /api/categories**: Retrieve a list of all categories.
- **GET /api/categories/{id}**: Retrieve details of a single category by ID.
- **POST /api/categories**: Create a new category (admin only).
- **PUT /api/categories/{id}**: Update an existing category (admin only).
- **DELETE /api/categories/{id}**: Delete a category (admin only).

### Item Endpoints

- **GET /api/items**: Retrieve a list of all available rental items.
- **GET /api/items/{id}**: Retrieve details of a single item by ID.
- **POST /api/items**: Create a new rental item (admin only).
- **PUT /api/items/{id}**: Update an existing rental item (admin only).
- **DELETE /api/items/{id}**: Delete a rental item (admin only).

### Rental Endpoints

- **POST /api/rentals**: Rent an item (user only).
- **GET /api/rentals/user/{userId}**: Retrieve all rentals by a specific user.
- **DELETE /api/rentals/{id}**: Cancel a rental (user/admin).

---

## Setup and Installation

### Prerequisites

- **Java 11 or higher**
- **Maven** (for dependency management)
- **MySQL/PostgreSQL** (or any other external database)

### Steps to Run Locally

1. **Clone the repository**:

   ```bash
   git clone https://github.com/your-username/rent-anything.git
   cd rent-anything
   ```

2. **Configure the Database**:

   Update the `application.properties` or `application.yml` file with your database connection details:

   ```bash
   spring.datasource.url=jdbc:mysql://localhost:3306/rentanything
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

3. **Install dependencies**:

   ```bash
   mvn clean install
   ```

4. **Run the application**:

   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**:
   - Visit `http://localhost:8080` in your browser.

## Testing

The project includes unit tests to verify key functionalities such as authentication, CRUD operations, and item rentals. Run the tests using:

```bash
mvn test
```

## Deployment

This project can be deployed to any external server like Heroku, AWS, or a personal VPS. Ensure your database is also hosted externally or accessible by the server.

## Future Improvements

- Search functionality: Allow users to search for items by name or category.
- Payment integration: Implement payment processing for renting items.
- Rental history: Provide users with a detailed history of their past rentals.
- Notifications: Email notifications for rental confirmations and reminders.

# -------------------------------------------------------

# RentAnything Setup Guide

      This guide explains how to set up the RentAnything application on your local development environment as well as deploying it to a production server.

## Local Development Setup

### 1. Clone the Repository

First, clone the repository to your local machine:

```sh
git clone https://github.com/Bminor87/rentanything.git
cd rentanything
```

### 2. Configure Application Properties

Navigate to the `src/main/resources` directory and create a copy of the `application-local.properties.example` file:

```sh
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

### 3. Update the Database Configuration

Open the newly created `application-local.properties` file and make sure the PostgreSQL configuration is correct. The file should look like this:

```properties
UPLOAD_DIR=uploads
DB_HOST=localhost
DB_PORT=5432
DB_NAME=rentanything
DB_USER=postgres
DB_PASSWORD=postgres

# Skip the prompt in the app startup - necessary for JUnit tests to run

app.skipPrompt=true
```

To load demo data on the first run: Set `app.skipPrompt=false` or comment out this line.

**Make sure the specified database in DB_NAME exists on your PostgreSQL server.**

### 4. Ensure the Upload Directory Exists

The application will look for an upload directory based on the `UPLOAD_DIR` property and the user's home directory. It is defined in code like this:

```java
public String getLocation() {

   String homeDir = System.getenv("HOME"); // /home/username in linux
   
   return homeDir + "/" + location; // location = UPLOAD_DIR
}
```

If the app fails here, there might be an issue with directory permissions

### 5. Run the Application

To start the application, use the Maven wrapper:

```sh
./mvnw spring-boot:run
```
# Production Server Setup

      To deploy the RentAnything application on a production server, follow these steps:

### 1. Configure the Systemd Service

Create a new systemd service file for RentAnything:

```sh
sudo nano /etc/systemd/system/rentanything.service
```

Add the following configuration:

```ini
[Unit]
Description=RentAnything Application
After=network.target

[Service]
User=YOUR_USER_NAME
WorkingDirectory=/home/YOUR_USER_NAME/{SITE_DOMAIN}
ExecStart=/home/YOUR_USER_NAME/{SITE_DOMAIN}/mvnw spring-boot

SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Replace `YOUR_USER_NAME` and `{SITE_DOMAIN}` with the appropriate values for your server setup.

## 2. Configure Nginx

Ensure Nginx is configured correctly to reverse proxy requests to the application. Create an Nginx server block for your site:

```sh
sudo nano /etc/nginx/sites-available/{SITE_DOMAIN}
```

Add the following configuration:

```nginx
server {
listen 80;
server_name {SITE_DOMAIN};

bash

location / {  
    proxy_pass http://localhost:8080;  
    proxy_set_header Host $host;  
    proxy_set_header X-Real-IP $remote_addr;  
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;  
    proxy_set_header X-Forwarded-Proto $scheme;  
}  

}
```

Enable the site and reload Nginx:

```sh
sudo ln -s /etc/nginx/sites-available/{SITE_DOMAIN} /etc/nginx/sites-enabled/
sudo systemctl reload nginx
```

### 3. Deploying Updates with the Deployment Script

Use the following script to deploy updates to the server:

```sh
cd ~/{SITE_DOMAIN}
git pull origin main
./mvnw clean package
echo "yourpassword" | sudo -S systemctl stop rentanything
echo "yourpassword" | sudo -S systemctl start rentanything
echo "yourpassword" | sudo -S systemctl daemon-reload
echo "🚀 Application deployed!"
```

Replace `{SITE_DOMAIN}` with the actual domain name of your site. Ensure the password (yourpassword) is secure and properly managed.

This is easily managed with [ploi.io](https://ploi.io)


## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/Bminor87/rentanything/blob/main/LICENSE) file for details.

#### This README.md was created with the help of ChatGPT
