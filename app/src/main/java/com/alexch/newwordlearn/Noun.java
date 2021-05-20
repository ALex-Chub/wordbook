package com.alexch.newwordlearn;

import androidx.recyclerview.widget.RecyclerView;

public class Noun {

    String art;
    String denoun;
    String rusnoun;
    String status;
    String category;
    String check;

    String htart;
    String htdenoun;
    String htrusnoun;

    public Noun(){}

    public Noun(String a,String de,String ru, String cat){
        this.art=a;
        this.denoun=de;
        this.rusnoun=ru;
        this.category=cat;
        this.status="learn";
    }

    void chengestatus(){if (status == "learn"){this.status = "Complieted";} else {this.status = "learn";}}

    public String getArt(){return this.art;}
    public String getDenoun() {return this.denoun;}
    public String getRusnoun(){return this.rusnoun; }
    public String getCategory(){return this.category;}
    public String getStatus(){return this.status;}

    public void setArt(String ar){this.art=ar;}
    public void setDenoun(String n){this.denoun=n;}
    public void setRusnoun(String r){this.rusnoun=r;}
    public void setCategory(String s){this.category=s;}

    public String getCheck() {return check;}

    public void setStatus(String status) {this.status = status;}
    public void setCheck(String check) {this.check = check;}

    public String gethtArt(){return this.htart;}
    public String gethtDenoun() {return this.htdenoun;}
    public String gethtRusnoun(){return this.htrusnoun;}

    public void sethtArt(String ar){this.htart=ar;}
    public void sethtDenoun(String n){this.htdenoun=n;}
    public void sethtRusnoun(String r){this.htrusnoun=r;}
}
