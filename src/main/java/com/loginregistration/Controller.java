package com.loginregistration;

import java.sql.*;
import java.util.Scanner;

public class Controller {
    Scanner scanner = new Scanner(System.in);
    Service service = new Service();
    public void yourChoice(){
        System.out.println("Hi!");
        System.out.println("Do you want to register(type 0) or have an account(type 1)?");
        while(true){
            String choice = scanner.nextLine();
            if(choice.equals("0") || choice.equals("1")){
                if(choice.equals("0")){
                    dataForRegistration();
                }else{
                    dataForLogin();
                }
                break;
            }else{
                System.out.println("Please, type 0/1");
            }
        }

        System.out.println("Do you want to take a book(0) or not(1)?");
        while(true){
            String choice = scanner.nextLine();
            if(choice.equals("0") || choice.equals("1")){
                if(choice.equals("0")){
                    chooseBook();
                }
                break;
            }else{
                System.out.println("Please, type 0/1");
            }
        }

        if(DAO.user.getRole().equals("admin")){
            System.out.println("Create - 0\n" +
                    "Read - 1\n" +
                    "Update - 2\n" +
                    "Delete - 3\n" +
                    "Quit - 4\n");
            while(true){
                int choice = scanner.nextInt();
                if(choice >= 0 && choice <= 3){
                    service.crud(choice);
                    System.out.println("\nChoose between 0 and 4: ");
                }else if(choice == 4) {
                    break;
                }else{
                    System.out.println("Please type between 0 and 4.");
                }
            }
        }

    }

    public void dataForRegistration(){
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter surname: ");
        String surname = scanner.nextLine();
        System.out.println("Enter email: ");
        String email = "";
        while(true){
            email = scanner.nextLine();
            if(email.contains("@")){
                break;
            }else{
                System.out.println("Please enter valid email address.");
            }
        }
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        System.out.println("Enter role: ");
        String role = "";
        while(true){
            role = scanner.nextLine();
            if(role.equals("admin") || role.equals("user")){
                break;
            }
            System.out.println("Please type admin/user ");
        }
        DAO.user = new Person(name, surname, email, username, password, role);
        service.loginOrRegister(true, null, null);
    }

    public void dataForLogin(){
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        service.loginOrRegister( false, username, password);
    }

    public void chooseBook(){
        System.out.println("Current books: ");
        Connection connection = DAO.connection();
        try{
            Statement statement = connection.createStatement();
            ResultSet books = statement.executeQuery("SELECT title FROM book");
            String titlesOfBooks = "";
            while(books.next()){
                titlesOfBooks += books.getString("title") + "\n";
            }
            System.out.println(titlesOfBooks);
            System.out.println("Write the title of book you wanted: ");
            while(true) {
                String bookChoice = scanner.nextLine();
                if (titlesOfBooks.toLowerCase().replace(" ", "").contains(bookChoice.toLowerCase().replace(" ", ""))) {
                    service.bookReservation(bookChoice);
                    break;
                }else{
                    System.out.println("There isn't such a book. \nPlease check your syntax or try another book.");
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
