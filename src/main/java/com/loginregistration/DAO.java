package com.loginregistration;

import java.sql.*;

public class DAO {
    String DB_PATH = "jdbc:mariadb://localhost:3306/login_registration";
    String USER = "root";
    String PASS = "chemasi";
    public void registerIntoDB(Person person){
        try (Connection connection = DriverManager.getConnection(DB_PATH, USER, PASS)){
//            Statement statement = connection.createStatement();
//            statement.execute("CREATE DATABASE IF NOT EXISTS login_registration");
//            statement.execute("use login_registration");
//            statement.execute("CREATE TABLE IF NOT EXISTS person(id INT NOT NULL UNIQUE AUTO_INCREMENT, name VARCHAR (20), surname VARCHAR (20), email VARCHAR (20), username VARCHAR (15) NOT NULL UNIQUE , password VARCHAR (10) UNIQUE NOT NULL)");

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO person(name, surname, email, username, password) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getSurname());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.setString(4, person.getUsername());
            preparedStatement.setString(5, person.getPassword());

            preparedStatement.executeQuery();
            System.out.println("Succeed!");


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void login(String username, String password){
        String message = "";

        PreparedStatement preparedStatement;
        try (Connection connection = DriverManager.getConnection(DB_PATH, USER, PASS)) {
            String command = "SELECT name, surname FROM person WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(command);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                message = "There is not such an account. Please, register first.";
            }else{
                String fullName = resultSet.getString("name") + " " + resultSet.getString("surname");
                message = "Successfully login Dear " + fullName;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(message);
    }
}
