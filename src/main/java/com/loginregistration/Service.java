package com.loginregistration;

public class Service {
    DAO dao = new DAO();

    public void loginOrRegister(boolean bool, String username, String password){
        if(bool){
            dao.registerIntoDB();
        }else{
            dao.login(username, password);
        }
    }

    public void bookReservation(String title){
        dao.book(title);
    }

    public void crud(int i){
        switch (i){
            case 0:
                dao.create();
                break;
            case 1:
                dao.read();
                break;
            case 2:
                dao.update();
                break;
            case 3:
                dao.delete();
                break;
        }
    }
}
