package com.loginregistration;

import com.github.javafaker.Faker;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Scanner;

public class DAO {
    static String DB_PATH = "jdbc:mariadb://localhost:3306/login_registration";
    static String USER = "root";
    static String PASS = "?????";
    static Person user = null;
    static int idOfLoggedIn = 0;
    Faker faker = new Faker();
    Scanner scanner = new Scanner(System.in);

    public static Connection connection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_PATH, USER, PASS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public void registerIntoDB() {
        Connection connection = connection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS login_registration");
            statement.execute("use login_registration");
            statement.execute("CREATE TABLE IF NOT EXISTS person(id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY, name VARCHAR (20), surname VARCHAR (20), email VARCHAR (20), username VARCHAR (15) NOT NULL UNIQUE , password VARCHAR (255) UNIQUE NOT NULL, role VARCHAR(10) NOT NULL)");

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO person(name, surname, email, username, password, role) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getRole());

            ResultSet resultSet = preparedStatement.executeQuery();

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT id FROM person WHERE password = ?");
            preparedStatement1.setString(1, user.getPassword());
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            while (resultSet1.next()) {
                idOfLoggedIn = resultSet1.getInt("id");
            }

            System.out.println("Succeed!");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void login(String username, String password) {
        String message = "";
        Connection connection = connection();
        try {
            String command0 = "SELECT password FROM person WHERE username = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(command0);
            preparedStatement1.setString(1, username);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            String hashedPassword = null;
            while (resultSet1.next()) {
                hashedPassword = resultSet1.getString("password");
            }
            if (BCrypt.checkpw(password, hashedPassword)) {

                String command = "SELECT * FROM person WHERE username = ? AND password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(command);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    message = "There is not such an account. Please, register first.";
                } else {
                    idOfLoggedIn = resultSet.getInt("id");
                    user = new Person(resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("email"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role")
                    );
                    String fullName = user.getName() + " " + user.getSurname();
                    message = "Successfully login: " + fullName;
                }
            }
            System.out.println(message);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void book(String title) {
        Connection connection = connection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("use login_registration");
            statement.execute("CREATE TABLE IF NOT EXISTS author(id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY, name VARCHAR (20), surname VARCHAR (30), age INT NOT NULL, number_of_books INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS book(id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY, title VARCHAR(100), published_year VARCHAR(10) , status ENUM('TAKEN', 'NOT_TAKEN'))");
            statement.execute("CREATE TABLE IF NOT EXISTS book_author(book_id INT UNIQUE REFERENCES book(id), author_id INT REFERENCES author(id))");
            statement.execute("CREATE TABLE IF NOT EXISTS user_book(user_id INT REFERENCES person(id), book_id INT UNIQUE REFERENCES book(id), start_day DATE , end_day DATE)");

            String command = "SELECT id FROM book WHERE LOWER(REPLACE(title, ' ', '')) = LOWER(REPLACE(?, ' ', '')) AND status = 'NOT_TAKEN'";
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int idForBook = resultSet.getInt("id");

                PreparedStatement bookWithChangedStatus = connection.prepareStatement("UPDATE book SET status = 'TAKEN' WHERE id = ?");
                bookWithChangedStatus.setInt(1, idForBook);
                bookWithChangedStatus.executeQuery();

                PreparedStatement insertIntoUserBook = connection.prepareStatement("INSERT INTO user_book(user_id, book_id, start_day, end_day) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY))");
                insertIntoUserBook.setInt(1, idOfLoggedIn);
                insertIntoUserBook.setInt(2, idForBook);
                insertIntoUserBook.executeQuery();

                System.out.println("Thank you:) You have to return the book within 15 days.");
            } else {
                System.out.println("We haven't this book at this moment :(");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

//    CRUD

    public void create() {
        String title = faker.book().title();
        String publishedYear = faker.random().nextInt(1900, 2021) + "";
        String[] author = faker.book().author().split(" ");
        String name = author[author.length - 1];
        String surname = author[author.length - 2];
        Integer age = faker.random().nextInt(20, 100);
        Integer numberOfBooks = faker.random().nextInt(1, 80);

        Connection connection = connection();
        try {
            PreparedStatement book = connection.prepareStatement("INSERT INTO book(title, published_year, status) VALUES (?, ?, 'NOT_TAKEN')");
            book.setString(1, title);
            book.setString(2, publishedYear);
            book.executeQuery();

            PreparedStatement authorPrepSt = connection.prepareStatement("INSERT INTO author(name, surname, age, number_of_books) VALUES (?, ?, ?, ?)");
            authorPrepSt.setString(1, name);
            authorPrepSt.setString(2, surname);
            authorPrepSt.setInt(3, age);
            authorPrepSt.setInt(4, numberOfBooks);
            authorPrepSt.executeQuery();

            PreparedStatement bookIdPrepSt = connection.prepareStatement("SELECT id FROM book WHERE title = ? AND published_year = ?");
            bookIdPrepSt.setString(1, title);
            bookIdPrepSt.setString(2, publishedYear);
            ResultSet resultSet = bookIdPrepSt.executeQuery();
            int bookId = 0;
            while (resultSet.next()) {
                bookId = resultSet.getInt("id");
            }

            PreparedStatement authorIdPrepSt = connection.prepareStatement("SELECT id FROM author WHERE name = ? AND surname = ?");
            authorIdPrepSt.setString(1, name);
            authorIdPrepSt.setString(2, surname);
            ResultSet resultSet1 = authorIdPrepSt.executeQuery();
            int authorId = 0;
            while (resultSet1.next()) {
                authorId = resultSet1.getInt("id");
            }

            PreparedStatement bookAuthor = connection.prepareStatement("INSERT INTO book_author(book_id, author_id) VALUES (?, ?)");
            bookAuthor.setInt(1, bookId);
            bookAuthor.setInt(2, authorId);
            bookAuthor.executeQuery();

            System.out.println("Successfully created!");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void read() {
        System.out.println("Read all(0) or read by author(1)");
        while (true) {
            String choiceForRead = scanner.nextLine();
            if (choiceForRead.equals("0") || choiceForRead.equals("1")) {
                if (choiceForRead.equals("0")) {
                    readAll();
                    System.out.println("\nSuccessfully read all books of all authors!");
                } else {
                    readByAuthor();
                }
                break;
            } else {
                System.out.println("Choose 0 or 1");
            }
        }
    }
    public void readAll() {
        Connection connection = connection();
        try {
            Statement statement = connection.createStatement();
            String command = "SELECT title, published_year, status, name, surname FROM book, author, book_author WHERE book.id = book_author.book_id AND author.id = book_author.author_id";
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                String bookWithAuthor = "";
                bookWithAuthor += resultSet.getString("title") + ", " +
                        resultSet.getString("published_year") + ", " +
                        resultSet.getString("status") + ", " +
                        resultSet.getString("name") + " " +
                        resultSet.getString("surname");
                System.out.println(bookWithAuthor);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void readByAuthor() {
        Connection connection = connection();
        try {
            Statement statement = connection.createStatement();
            ResultSet authors = statement.executeQuery("SELECT CONCAT(name, ' ', surname) AS full_name FROM author");
            String fullNamesOfAuthors = "";
            while(authors.next()){
                fullNamesOfAuthors += authors.getString("full_name") + "\n";
            }
            System.out.println(fullNamesOfAuthors);

            System.out.println("Write the name and surname of the author: ");
            String author = scanner.nextLine();

            String command = "SELECT title, published_year, status, name, surname FROM author, book, book_author WHERE book_id = book.id AND author_id = author.id AND LOWER(REPLACE(CONCAT(name, surname), ' ', '')) = LOWER(REPLACE(?, ' ', ''))";
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            preparedStatement.setString(1, author);
            ResultSet resultSet = preparedStatement.executeQuery();
            String info = "";
            while (resultSet.next()) {
                info = resultSet.getString("title") + ", " +
                        resultSet.getString("published_year") + ", " +
                        resultSet.getString("status") + ", " +
                        resultSet.getString("name") + " " +
                        resultSet.getString("surname");
                System.out.println(info);
            }
            if(info.equals("")){
                System.out.println("If your SYNTAX is VALID then now we haven't a book of this author. Please try another day.");
            }else{
                System.out.println("Successfully read all books of the mentioned author!");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void update() {
        Connection connection = connection();
        try {
            Statement statement = connection.createStatement();
            ResultSet books = statement.executeQuery("SELECT title FROM book");
            String titlesOfBooks = "";
            while(books.next()){
                titlesOfBooks += books.getString("title") + "\n";
            }
            System.out.println(titlesOfBooks);

            System.out.println("Write the title of the book that you want to update: ");
            String updatedBook = scanner.nextLine();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM book WHERE LOWER(REPLACE(title, ' ', '')) = LOWER(REPLACE(?, ' ', '')) AND status = 'NOT_TAKEN'");
            preparedStatement.setString(1, updatedBook);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("There isn't such a book that is not taken. Please check your syntax.");
            } else {

                int idForUpdate = resultSet.getInt("id");

                PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE book SET status = 'TAKEN' WHERE id = ?");
                preparedStatement1.setInt(1, idForUpdate);

                ResultSet resultSet1 = preparedStatement1.executeQuery();
                System.out.println("\nSuccessfully updated!");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void delete() {
        Connection connection = connection();

        try {
            Statement statement = connection.createStatement();
            ResultSet books = statement.executeQuery("SELECT title FROM book");
            String titlesOfBooks = "";
            while(books.next()){
                titlesOfBooks += books.getString("title") + "\n";
            }
            System.out.println(titlesOfBooks);

            System.out.println("Write the title of the book that you want to delete: ");
            String updatedBook = scanner.nextLine();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM book WHERE LOWER(REPLACE(title, ' ', '')) = LOWER(REPLACE(?, ' ', '')) AND status = 'NOT_TAKEN'");
            preparedStatement.setString(1, updatedBook);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("There isn't such a book. Please check your syntax.");
            } else {
                int idForDelete = resultSet.getInt("id");
                System.out.println(idForDelete);
                PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM book_author WHERE book_id = ?");
                preparedStatement1.setInt(1, idForDelete);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM book WHERE id = ?");
                preparedStatement2.setInt(1, idForDelete);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                System.out.println("\nSuccessfully deleted!");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
