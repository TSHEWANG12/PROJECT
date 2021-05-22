package com.gcit.funcraft;

public class Craft {
    private String CraftName;
    private  String ImageUrl;
    private String Description;
    private String Cost;
    private String Number;
    public Craft(String craftName,String imageUrl,String description,String cost,String number){
        CraftName=craftName;
        ImageUrl=imageUrl;
        Description=description;
        Cost=cost;
        Number=number;
    }
    public String getCraftName(){

        return CraftName;
    }

    public void setCraftName(String craftName)
    {

        CraftName = craftName;
    }

    public String getImageUrl() {

        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {

        ImageUrl = imageUrl;
    }
    public String getDescription(){

        return Description;
    }
    public void setDescription(String description){

        Description=description;
    }
    public String getCost(){
        return Cost;
    }
    public String getNumber(){
        return Number;
    }



    public Craft() {

    }

}

