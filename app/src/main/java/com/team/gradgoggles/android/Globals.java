package com.team.gradgoggles.android;

public class Globals {

    private static Globals instance;
    private String postedbyStduent_id;
    private Boolean searchByYearbook = true;
    private Boolean searchByName = true;
    private Boolean searchByDept = false;
    private String searchParam = null;


    private int yearbookpageno =1;
    private int namepageno=1;
    private int deptpageno=1;

    private Globals(){}
    public void setCurrentYearbookPage(int page){this.yearbookpageno =page;}
    public int getCurrentYearbookPage(){return  this.yearbookpageno;}

    public void setCurrentNamePage(int page){this.namepageno =page;}
    public int getCurrentNamePage(){return  this.namepageno;}

    public void setCurrentDeptPage(int page){this.deptpageno =page;}
    public int getCurrentDeptPage(){return  this.deptpageno;}


   public void setPostedbyStduent_id(String id){
        this.postedbyStduent_id = id;
    }
   public String getPostedbyStduent_id(){
        return this.postedbyStduent_id;
    }



    public void setSearchByYearbook(Boolean flag){this.searchByYearbook=flag;}
    public Boolean getSearchByYearbook(){return  this.searchByYearbook;}



    public void setSearchByName(Boolean flag){this.searchByName=flag;}
    public Boolean getSearchByName(){return this.searchByName;}

    public void setSearchByDept(Boolean flag){this.searchByDept = flag;}
    public Boolean getSearchByDept(){return this.searchByDept;}


    public void setSearchParam(String Param){this.searchParam =Param;}
    public String getSearchParam(){return  this.searchParam;}

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance = new Globals();
        }
        return instance;
    }

}
