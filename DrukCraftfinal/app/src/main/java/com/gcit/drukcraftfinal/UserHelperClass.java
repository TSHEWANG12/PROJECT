package com.gcit.drukcraftfinal;

public class UserHelperClass {
    String inputUsername,inputEmail,inputPassword,inputConfirmPassword,inputCall, inputLicenseno;

    public UserHelperClass() {

    }

    public UserHelperClass(String inputUsername, String inputEmail, String inputPassword, String inputConfirmPassword, String inputCall, String inputAddress) {
        this.inputUsername = inputUsername;
        this.inputEmail = inputEmail;
        this.inputPassword = inputPassword;
        this.inputConfirmPassword = inputConfirmPassword;
        this.inputCall = inputCall;
        this. inputLicenseno =  inputLicenseno;
    }
    public String getInputUsername() {
        return inputUsername;
    }

    public void setInputUsername(String inputUsername) {
        this.inputUsername = inputUsername;
    }

    public String getInputEmail() {
        return inputEmail;
    }

    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    public String getInputConfirmPassword() {
        return inputConfirmPassword;
    }

    public void setInputConfirmPassword(String inputConfirmPassword) {
        this.inputConfirmPassword = inputConfirmPassword;
    }

    public String getInputCall() {
        return inputCall;
    }

    public void setInputCall(String inputCall) {
        this.inputCall = inputCall;
    }

    public String getInputAddress() {
        return  inputLicenseno;
    }

    public void setInputAddress(String inputAddress) {
        this. inputLicenseno = inputAddress;
    }
}


