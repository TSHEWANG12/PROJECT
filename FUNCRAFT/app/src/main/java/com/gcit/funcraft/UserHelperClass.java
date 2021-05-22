package com.gcit.funcraft;

public class UserHelperClass {
    String inputUsername,inputEmail,inputPassword,inputCall, inputLicenseno;

    public UserHelperClass() {

    }

    public UserHelperClass(String inputUsername, String inputEmail, String inputPassword, String inputLicenseno, String inputCall) {
        this.inputUsername = inputUsername;
        this.inputEmail = inputEmail;
        this.inputPassword = inputPassword;
        this. inputLicenseno =  inputLicenseno;
        this.inputCall = inputCall;
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

    public String getInputLicenseno() {
        return inputLicenseno;
    }

    public void setInputLicenseno(String inputLicenseno) {
        this.inputLicenseno = inputLicenseno;
    }

    public String getInputCall() {

        return inputCall;
    }

    public void setInputCall(String inputCall) {
        this.inputCall = inputCall;
    }
}
