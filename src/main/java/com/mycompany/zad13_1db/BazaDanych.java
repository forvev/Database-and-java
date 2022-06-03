package com.mycompany.zad13_1db;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class BazaDanych {
    
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL ="jdbc:sqlite:C:\\Users\\Artur\\Desktop\\studia\\III semestr\\Programowanie obiektowe w Java\\laby-zadania\\lab13DB\\zad13_1DB\\DB.db";
    private Connection connection;
    private Statement statement;
    
    public BazaDanych() {
        
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            
            System.err.println("there is no such a driver like JDBC");
            ex.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            tworzTabele();
        
        } catch (SQLException ex) {
            System.err.println("Problem z otwarciem połączenia");
            ex.printStackTrace();
        }
    }
    
    public boolean tworzTabele() {
        String tworz = "CREATE TABLE IF NOT EXISTS STUDENCI(id INTEGER PRIMARY KEY AUTOINCREMENT, nazwisko String, imie String)";
        try {
            statement.execute(tworz);
        } catch (SQLException e) {
            System.err.println("Błąd przy tworzeniu tabeli");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void usunStudenta(String tabela, String nazwisko, String imie){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + tabela + " WHERE nazwisko=? and imie=?");
            preparedStatement.setString(1, nazwisko);
            preparedStatement.setString(2, imie);
            preparedStatement.execute();
            
        }catch(SQLException e){
            System.err.println("Błąd przy wprowadzaniu danych studenta: " + nazwisko + " " + imie);
            e.printStackTrace();
        }
    }
    
    public void modyfikujStudenta(String tabela, String nazwisko, String imie, String new_n, int tryb){
        try{
            PreparedStatement preparedStatement;
            if(tryb==1){//zmienianmy imie
                 preparedStatement = connection.prepareStatement("UPDATE " + tabela + " SET imie=? WHERE nazwisko=? and imie=?");
                
            }
            else{
                preparedStatement = connection.prepareStatement("UPDATE " + tabela + " SET nazwisko=? WHERE nazwisko=? and imie=?");
                
            }
            preparedStatement.setString(1, new_n);
            preparedStatement.setString(2, nazwisko);
            preparedStatement.setString(3, imie);
            preparedStatement.execute();
            //PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + tabela + " WHERE nazwisko=? and imie=?");
            
            
        }catch(SQLException e){
            System.err.println("Bład przy wprowadzaniu danych studenta: " + nazwisko + " " + imie);
            e.printStackTrace();
        }
    }
    
    public boolean wstawDane(String tabela, String nazwisko, String imie) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + tabela + " VALUES(null,?,?)");
            preparedStatement.setString(1, nazwisko);
            preparedStatement.setString(2, imie);
            preparedStatement.execute();
         } catch (SQLException e) {
            System.err.println("Bład przy wprowadzaniu danych studenta: " + nazwisko + " " + imie);
            e.printStackTrace();
            return false;
         }
        return true;
    }
    
    public List<Student> pobierzDane(String tabela) {
        
        List<Student> wyjscie = new LinkedList<Student>();
        try {
            ResultSet resultSet =statement.executeQuery("SELECT * FROM " + tabela);
            int id;
            String nazwisko, imie;
            
            while (resultSet.next()) {
                
                id = resultSet.getInt("id");
                nazwisko = resultSet.getString("nazwisko");
                imie = resultSet.getString("imie");
                wyjscie.add(new Student(id, nazwisko, imie));
            }
        } catch (SQLException e) {
            System.err.println("Problem z wczytaniem danych z BD");
            e.printStackTrace();
            return null;
        }
        return wyjscie;
    }
    
    public List<Student> pobierzDane(String tabela, String lastname, String name){
        
        List<Student> wyjscie = new LinkedList<Student>();
        try{
            ResultSet resultSet = statement.executeQuery("SELECT * FROM "+tabela+" WHERE nazwisko=\'"
                    + lastname+"\' AND imie=\'"+name+"\'");
            

            int id;
            String db_name, db_lastname;
            
            while(resultSet.next()){
                id=resultSet.getInt("id");
                db_lastname = resultSet.getString("nazwisko");
                db_name = resultSet.getString("imie");
                wyjscie.add(new Student(id, db_lastname, db_name));
            }
            
        }catch(SQLException e){
            System.err.println("Problem z wczytaniem danych z BD");
            e.printStackTrace();
            return null;
        }
        
        return wyjscie;
    }
    
    
    public void zamknijPolaczenie() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Problem z zamknieciem połączenia");
            e.printStackTrace();
        }
    }
}
