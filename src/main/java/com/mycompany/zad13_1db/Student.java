package com.mycompany.zad13_1db;

public class Student {
    private int id;
    private String nazwisko, imie;
    
    public Student(int id, String nazwisko, String imie) {
        this.id = id;
        this.nazwisko = nazwisko;
        this.imie = imie;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNazwisko() {
        return nazwisko;
    }
    
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
    
    public String getImie() {
        return imie;
    }
    
    public void setImie(String imie) {
        this.imie = imie;
    }
}
