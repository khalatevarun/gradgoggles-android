package com.team.gradgoggles.android;

public class PostedBy {
   // @Json(name = "name")
    private String name;
   // @Json(name = "photo")
    private String photo;

    private int id;

    public PostedBy(String mname, String mphoto, int mid){
        name = mname;
        photo=mphoto;
        id=mid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public int getId(){return id;}

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
