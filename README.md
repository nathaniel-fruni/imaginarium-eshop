# Imaginarium - E-shop

Welcome to the Imaginarium E-shop!

## Sample Login Credentials

### Customer Account
- **Username:** lukas.kras@ukf.sk
- **Password:** 123

### Admin Account
- **Username:** martin.novak@ukf.sk
- **Password:** 789

## Dependencies

This project uses the BCrypt library for secure password hashing. You can download the BCrypt JAR file from [BCrypt Maven Repository](https://mvnrepository.com/artifact/org.mindrot/jbcrypt).

## Features

This README provides details about the project:

### Technologies Used

This Dynamic Web Project is built using JavaEE, with a focus on the following key components:

- **Java Servlets:** The backend logic, server-side processing, and dynamic content generation are implemented using Java Servlets
- **MySQL Connector/J:** To interact with the database, the project utilizes the MySQL Connector/J library.

### Project Structure

The project is organized with the following structure:

- `src/`: Contains the source code for the project.
  - `main/`
    - `java/`: Java Servlets source code.
      - `/admin`: Admin related Servlets.
      - `/customer`: Customer related Servlets.
      - `/listener`: Listeners.
      - `/util`: Utility classes.
   - `webapp/`: Conatins web assets.
     - `images/`
     - `js/`
     - `META-INF/`
     - `styles/`
     - `WEB-INF/`
     - `admin.html`: Admin login page.
     - `index.html`: Main login page.
- `database/`: Contains sql file for database. import.


### Key Features

#### User account
- Log in.
- View a list of books.
- View detail of a book.
- Place book to basket.
- See basket content. (edit basket content)
- Place an order.
- See list or order.
- See order detail.

#### Admin account
- Log in.
- View list of users and their orders.
- View order detail. (edit orders)
- Change user's role.

### Tools Used

This website was built using [Pingendo](https://pingendo.com).

### Image credits

This project uses free pictures from [Pixabay](https://pixabay.com).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.