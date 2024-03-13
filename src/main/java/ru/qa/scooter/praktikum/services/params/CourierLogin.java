package ru.qa.scooter.praktikum.services.params;

public class CourierLogin {

    String login;
    String password;

    public CourierLogin(String login, String password){
        this.login = login;
        this.password = password;
    }


    public CourierLogin(){

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
