package com.alexch.newwordlearn;


public class Verbs {

    String nomenativ;
    String form2;
    String form3;
    String ruverb;
    String category;
    String status;
    String check;
    String htnomenativ;
    String htform2;
    String htform3;
    String htruverb;

    public Verbs(){}
    public Verbs(String n, String f2, String f3, String ru, String category){
        this.nomenativ = n;
        this.form2 = f2;
        this.form3 = f3;
        this.ruverb = ru;
        this.category = category;
        this.status="learn";
    }

    void chengestatus(){
        if (status == "learn"){
            this.status = "Complieted";
        } else {
            this.status = "learn";
        }

    }

    public String getNomenativ(){return this.nomenativ;};
    public String getForm2(){return this.form2;}
    public String getForm3(){return this.form3;}
    public String getRuverb(){return this.ruverb;}
    public String getCategory(){return this.category;}
    public String getStatus(){return  this.status;}
    public String getCheck() {
        return this.check;
    }

    public String gethtNomenativ(){return this.htnomenativ;};
    public String gethtForm2(){return this.htform2;}
    public String gethtForm3(){return this.htform3;}

    public String gethtRuverb(){return this.htruverb;}

    public void  setNomenativ(String ar){this.nomenativ=ar;}

    public void  setForm2(String n){this.form2=n;}

    public void  setForm3(String r){this.form3=r;}

    public void setCategory(String s){this.category=s;}

    public void setRuverb(String r){this.ruverb=r;}

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCheck(String check) {
        this.check = check;
    }
    public void  sethtNomenativ(String ar){this.htnomenativ=ar;}
    public void  sethtForm2(String n){this.htform2=n;}
    public void  sethtForm3(String r){this.htform3=r;}
    public void sethtRuverb(String r){this.htruverb=r;}
}
