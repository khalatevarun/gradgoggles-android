package com.team.gradgoggles.android;

public class Scrap {
  //  @Json(name = "id")
    private String id;
    private int idBy;
   // @Json(name = "posted_by")
    public PostedBy postedBy;

 //   @Json(name = "content")
    private String content;
 //   @Json(name = "timestamp")
    private String timestamp;
   // @Json(name = "visibility")
    private Boolean visibility;

    public Scrap(String mcontent,String mid, String mtimestamp, PostedBy mpostedBy, Boolean mvisibility){
        content=mcontent;
        timestamp=mtimestamp;
        postedBy=mpostedBy;
        id=mid;
        visibility = mvisibility;
    }
    public Scrap(String mcontent,int mid, String mtimestamp, PostedBy mpostedBy){
        content=mcontent;
        timestamp=mtimestamp;
        postedBy=mpostedBy;
        idBy=mid;
    }

    public String getId() {
        return id;
    }

    public Integer getIdBy(){return  idBy;}

    public void setId(String id) {
        this.id = id;
    }

    public PostedBy getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(PostedBy postedBy) {
        this.postedBy = postedBy;
    }

    public  Boolean getVisibility(){ return  visibility;}



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

}