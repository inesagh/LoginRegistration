package com.loginregistration;

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

    }

    public void dataForRegistration(){
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter surname: ");
        String surname = scanner.nextLine();
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        Person person = new Person(name, surname, email, username, password);

        service.loginOrRegister(person, true, null, null);
    }

    public void dataForLogin(){
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        service.loginOrRegister(null, false, username, password);
    }
}
