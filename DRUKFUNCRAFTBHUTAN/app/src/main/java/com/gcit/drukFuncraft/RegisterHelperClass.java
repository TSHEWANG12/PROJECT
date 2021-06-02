package com.gcit.drukFuncraft;

public class RegisterHelperClass {
    //All variable declaration
    private String FullName, Email, License, Contact, Password, PhotoUri;
    public RegisterHelperClass(){

    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLicense() {
        return License;
    }

    public void setLicense(String license) {
        License = license;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getPhotoUri() {
        return PhotoUri;
    }

    public void setPhotoUri(String photoUri) {
        PhotoUri = photoUri;
    }

    public RegisterHelperClass(String FullName, String Email, String License, String Contact, String Password, String photo){
        this.FullName = FullName;
        this.Email = Email;
        this.License = License;
        this.Contact = Contact;
        this.Password = Password;
        this.PhotoUri = photo;
    }
}
