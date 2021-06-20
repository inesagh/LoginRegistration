package com.loginregistration;

public class Service {
    public void loginOrRegister(Person person, boolean bool, String username, String password){
        DAO dao = new DAO();
        if(bool){
            dao.registerIntoDB(person);
        }else{
            dao.login(username, password);
        }
    }
}
