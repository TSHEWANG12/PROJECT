package com.gcit.drukcraftfinal;

import java.security.PrivateKey;

public class Craft {
    private String CraftName;
    private  String ImageUrl;
    public Craft(String craftName,String imageUrl){
        CraftName=craftName;
        ImageUrl=imageUrl;
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

    public Craft() {

    }
}
