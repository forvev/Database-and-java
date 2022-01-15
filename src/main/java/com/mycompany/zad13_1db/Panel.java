package com.mycompany.zad13_1db;

import java.awt.CardLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;

public class Panel extends JPanel{
    
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL ="jdbc:sqlite:C:\\Users\\Artur\\Desktop\\studia\\III semestr\\Programowanie obiektowe w Java\\laby-zadania\\lab13DB\\zad13_1DB\\DB.db";
    private Connection connection;
    private Statement statement;
    private JTextArea message;
    private JButton add_new_students, read_students, modify_students, confirm_students;
    private JPanel panel_button, textArea, add_studentsPanel;
    private GridBagConstraints c;
    private JScrollPane przewijanie;
    private JLabel e1,e2;
    private JTextField textName, textLastname;
    
    Panel(){
         //setBackground(Color.LIGHT_GRAY);
        
         add_new_students = new JButton("Add new student");
         add_new_students.addActionListener(new Listener(1));
         read_students = new JButton("Read the given student");
         read_students.addActionListener(new Listener(2));
         modify_students = new JButton("Modift the given student");
         modify_students.addActionListener(new Listener(3));       

         panel_button = new JPanel();
         panel_button.setLayout(new GridBagLayout()); //new GridLayout(3, 1, 10, 5)
         
         textArea = new JPanel(new GridBagLayout());
         this.setLayout(new BorderLayout());
         c = new GridBagConstraints();
         
         message = new JTextArea(10,20);
         message.setLineWrap(true);
         message.setWrapStyleWord(true);
         message.setEditable(false);
         przewijanie = new JScrollPane(message);
         
         przewijanie.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
         przewijanie.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
         
         setWindow();
         dataBase();
    }
    
    public void setWindow(){
        textArea.add(przewijanie);
         
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(2,2,2,2);
         
        panel_button.add(add_new_students, c);
         
        c.gridx = 1;
        c.gridy = 0;
         
        panel_button.add(read_students, c);
         
        c.gridx = 2;
        c.gridy = 0;
         
        panel_button.add(modify_students, c);
         
        this.add(textArea, BorderLayout.WEST);
         
        this.add(panel_button, BorderLayout.NORTH);
    }
    
    private class Listener implements ActionListener{
        
        int method;
        
        Listener(int i){
            method=i;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(method==1){
                add_students(1);
                
            }
            else if(method==2){
                add_students(2);
            }
            else if(method==3){
                
            }
        }
        
    }
    
    private void add_students(int method){
        add_studentsPanel = new JPanel(new GridBagLayout());
        
        e1 = new JLabel("Name:");
        c.gridx=0;
        c.gridy=0;         
        add_studentsPanel.add(e1,c);
         
        e2 = new JLabel("Lastname:");       
        c.gridx=0;
        c.gridy=1;     
        add_studentsPanel.add(e2,c);
        
        textName = new JTextField(10);
        c.gridx=1;
        c.gridy=0;
        add_studentsPanel.add(textName,c);
        
        textLastname = new JTextField(10);
        c.gridx=1;
        c.gridy=1;
        add_studentsPanel.add(textLastname,c);
        
        confirm_students = new JButton("OK");
        if(method==1) confirm_students.addActionListener(new ListenerDB(1));
        else if(method==2) confirm_students.addActionListener(new ListenerDB(2));
        c.gridx=1;
        c.gridy=2;
        add_studentsPanel.add(confirm_students,c);
        
        System.out.println("siema");
        this.add(add_studentsPanel, BorderLayout.CENTER);
        this.validate(); //po nacisnieciu przycisku ukaza nam sie pola do wpisania
        
        
    }
    
    private class ListenerDB implements ActionListener{//dodajemy do BD
        int method;
        
        ListenerDB(int i){
            method=i;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(method==1) readStudents_method();
            else if(method==2){
                List<Student> wyjscie = new LinkedList<Student>();
                wyjscie = pobierzDane("Studenci");
                
                for(Student s: wyjscie){
                    message.setText(s.getImie()+" "+s.getNazwisko()+", id:"+s.getId()+"\n");
                }
            }
        }
        
    }
    public List<Student> pobierzDane(String tabela) {
        
        String name= textName.getText();
        String lastname = textLastname.getText();
                
        java.util.List<Student> wyjscie = new LinkedList<Student>();
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
    
    public void readStudents_method(){
        String lastname_p= textLastname.getText();
            String name_p = textName.getText();
            try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + "Studenci" + " VALUES(null,?,?)");
            preparedStatement.setString(1, lastname_p);
            preparedStatement.setString(2, name_p);
            preparedStatement.execute();
         } catch (SQLException exp) {
            System.err.println("Błąd przy wprowadzaniu danych studenta: " + lastname_p + " " + name_p);
            exp.printStackTrace();
         }
    }
    
    public void dataBase(){
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            
            System.err.println("Brak sterownika JDBC");
            ex.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            createTable();
        
        } catch (SQLException ex) {
            System.err.println("Problem z otwarciem połączenia");
            ex.printStackTrace();
        }
    }
    
    public void createTable(){
        String tworz = "CREATE TABLE IF NOT EXISTS STUDENCI(id INTEGER PRIMARY KEY AUTOINCREMENT, nazwisko String, imie String)";
        try {
            statement.execute(tworz);
        } catch (SQLException e) {
            System.err.println("Błąd przy tworzeniu tabeli");
            e.printStackTrace();
        }

    }
    
}
