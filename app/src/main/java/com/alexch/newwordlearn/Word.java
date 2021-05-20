package com.alexch.newwordlearn;

public class Word {

    String userid;
    String deutschword; // слово на немецком
    String ruschword;  // слово на русском
    String catogory; // категория
   String status;  // статус "выучено" - да - уходит в архив, нет - выводится для повторения
    String check; // только для обработки при обучении
    String htdeutschword;
    String htruschword;


    public Word(){}
    public Word(String dw, String rw, String cat){

       this.deutschword = dw;
        this.ruschword = rw;
        this.catogory = cat;
       this.status = "learn";

    }
    public Word(String dw, String rw, String cat, String userId){

        this.deutschword = dw;
        this.ruschword = rw;
        this.catogory = cat;
        this.status = "learn";
        this.userid=userId;


    }
    void chengestatus(){
        if (status == "learn"){
            this.status = "Complieted";
        } else {
            this.status = "learn";
        }

    }

    public String getdeutschword() {
        return this.deutschword;
    }

    public String getruschword() {
        return this.ruschword;
    }

    public String getUserid(){return this.userid;}


    public String getCatogory() {
        return this.catogory;
    }

    public String getstatus(){return this.status;}

    public String getCheck() {
        return this.check;
    }

    public String gethtdeutschword() {
        return this.htdeutschword;
    }

    public String gethtruschword() {
        return this.htruschword;
    }


    public void  setDeutschword(String d){this.deutschword=d;}

    public void  setRuschword(String r){this.ruschword=r;}

    public void  setCatogory(String c){this.catogory=c;}

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public void  settDeutschword(String d){this.htdeutschword=d;}

    public void  settRuschword(String r){this.htruschword=r;}
}
