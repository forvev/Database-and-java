package com.mycompany.zad13_1db;

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
    private JButton add_new_students, read_students, modify_students, confirm_students, jb_delete, jb_modify;
    private JPanel panel_button, textArea, add_studentsPanel, lable_add, final_panel;
    private GridBagConstraints c;
    private JScrollPane przewijanie;
    private JLabel e1,e2,info_modify;
    private JTextField textName, textLastname;
    private int counter_modify;
    private JCheckBox cb1;
    private ListenerDB listener_1, listener_2, listener_3, listener_4; //add,read,delete, modify
    private String name_before, surname_before;
    
    Panel(){
         name_before = new String();
         surname_before = new String();
         counter_modify=0;
         
         add_new_students = new JButton("Add new student");
         add_new_students.addActionListener(new Listener(1));
         read_students = new JButton("Read the given student");
         read_students.addActionListener(new Listener(2));
         modify_students = new JButton("Modift the given student");
         modify_students.addActionListener(new Listener(3));
         
         add_studentsPanel = new JPanel(new GridBagLayout());
         jb_delete = new JButton("Delete");
         confirm_students = new JButton("OK");
         jb_modify = new JButton("Modify");
         
         
         cb1 = new JCheckBox("Clear");

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
         
         cb1.addItemListener( e -> {
             message.setText("");
        });
         
         przewijanie.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
         przewijanie.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
             
         setWindow_left();
         setWindow_right();
         dataBase();
    }
    
    public void setWindow_right(){
        lable_add = new JPanel();
        info_modify = new JLabel("Now, provide the new data and click modify!");
        
        lable_add.add(info_modify);
        
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
        
        c.gridx=1;
        c.gridy=2;
        add_studentsPanel.add(confirm_students,c);
        
        c.gridx=0;
        c.gridy=2;
        add_studentsPanel.add(jb_delete,c);
        c.gridx=1;
        c.gridy=2;
        add_studentsPanel.add(jb_modify,c);
        
        final_panel = new JPanel( new BorderLayout());     
        
        final_panel.add(add_studentsPanel,BorderLayout.CENTER);
        final_panel.add(lable_add, BorderLayout.NORTH);
        
        this.add(final_panel, BorderLayout.CENTER);
        this.validate(); //po nacisnieciu przycisku ukaza nam sie pola do wpisania
        
        
        e1.setVisible(false); e2.setVisible(false); 
        textName.setVisible(false);
        textLastname.setVisible(false);
        confirm_students.setVisible(false);
        jb_delete.setVisible(false);
        jb_modify.setVisible(false);
        info_modify.setVisible(false);
        
    }

    public void setWindow_left(){ //left side
        
        
        c.gridx=0;
        c.gridy=0;
        textArea.add(przewijanie,c);
         
        c.gridx=0;
        c.gridy=1;
        textArea.add(cb1,c);
        
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
            e1.setVisible(true); e2.setVisible(true); 
            textName.setVisible(true);
            textLastname.setVisible(true);
            info_modify.setVisible(false);
            
            textName.setText("");
            textLastname.setText("");
            if (method!=3){
                jb_delete.setVisible(false);
                jb_modify.setVisible(false);
                confirm_students.setVisible(true);
                if(method==1){
                    add_new_students.setBackground(Color.GREEN);
                    read_students.setBackground(null);
                    modify_students.setBackground(null);  
                    confirm_students.removeActionListener(listener_2);
                    confirm_students.removeActionListener(listener_1);
                    add_students(1);

                }
                else {  
                    read_students.setBackground(Color.GREEN);
                    add_new_students.setBackground(null);
                    modify_students.setBackground(null);
                    //to avoid double listeners
                    confirm_students.removeActionListener(listener_1);
                    confirm_students.removeActionListener(listener_2);
                    add_students(2);
                }
            }
            else{
                modify_students.setBackground(Color.GREEN);
                add_new_students.setBackground(null);
                read_students.setBackground(null);
                jb_delete.setVisible(true);
                jb_modify.setVisible(true);
                confirm_students.setVisible(false);
                info_modify.setVisible(false);
                jb_delete.removeActionListener(listener_3);
                jb_modify.removeActionListener(listener_4);
                add_students(3);
            }
        }
        
    }
    
    private void add_students(int method){ 
         
        
        if(method==1){
            listener_1 = new ListenerDB(1);
            confirm_students.addActionListener(listener_1);
        }
        else if(method==2){
            listener_2 = new ListenerDB(2);
            confirm_students.addActionListener(listener_2);
             
        }
        else if(method==3){
            listener_3 = new ListenerDB(3);
            jb_delete.addActionListener(listener_3);
            //jb_delete.addActionListener(g -> {
 
            //});
            listener_4 = new ListenerDB(4);
            jb_modify.addActionListener(listener_4);
            //jb_modify.addActionListener(l->{
                
           // });          
        }       
   }
    
    private class ListenerDB implements ActionListener{//add to BD
        int method;
        
        ListenerDB(int i){
            method=i;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(method==1){
                if(textLastname.getText().isEmpty() && textName.getText().isEmpty()) message.append("Provide some value!\n");
                else readStudents_method();
                   
            }
            else if(method==2){
                System.out.println("2");
                List<Student> wyjscie = new LinkedList<Student>();
                wyjscie = pobierzDane("Studenci");
                
                for(Student s: wyjscie){
                    message.append(s.getImie()+" "+s.getNazwisko()+", id:"+s.getId()+"\n");              
                }
                System.out.println("czemu tu jestem");
                if(wyjscie.isEmpty()) message.append("There is no such a user!!\n");
            }
            else if(method==3){
                if(!textLastname.getText().isEmpty() || !textName.getText().isEmpty()){
                    System.out.println("nie wiem");
                    String lastname_p= textLastname.getText();
                    String name_p = textName.getText();
                    try{
                        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + "Studenci" + " WHERE nazwisko=? and imie=?");
                        preparedStatement.setString(1, lastname_p);
                        preparedStatement.setString(2, name_p);
                        preparedStatement.execute();

                    }catch(SQLException ee){
                        System.err.println("Error during providing a user data: " + lastname_p + " " + name_p);
                        ee.printStackTrace();
                    }
                }
                else{
                    System.out.println("no jak");
                    
                    message.append("Provide some value!\n");
                }
            }
            
            else if(method==4){
                counter_modify++;
                System.out.println("ok, zacznam");
                if((counter_modify!=2)){
                    System.out.println("Zmieniam1");
                    name_before = textName.getText();
                    surname_before = textLastname.getText();
                    textLastname.setText("");
                    textName.setText("");
                    info_modify.setVisible(true);
                    System.out.println("pobrałem:"+ name_before + surname_before);
                }
                else{
                    System.out.println("Zmieniam2");
                    counter_modify=0;
                    info_modify.setVisible(false);
                    String lastname_p= textLastname.getText();
                    textLastname.setText("");
                    String name_p = textName.getText();
                    textName.setText("");
                    
                    System.out.println("a teraz :"+ lastname_p + name_p);
                    
                    try{
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + "Studenci" + " SET nazwisko=?, imie=? WHERE nazwisko=? and imie=?");
                        System.out.println(lastname_p);
                    preparedStatement.setString(1, lastname_p);
                    System.out.println(name_p);
                    preparedStatement.setString(2, name_p);
                    System.out.println(surname_before);
                    preparedStatement.setString(3, surname_before);
                    System.out.println(name_before);
                    preparedStatement.setString(4, name_before);
                    preparedStatement.execute();

                }catch(SQLException g){
                    System.err.println("Error during data's transmission: " + lastname_p + " " + name_p);
                    g.printStackTrace();
                }
            }
            }
        }
        
    } 
    public List<Student> pobierzDane(String tabela) {
        
        String name= textName.getText();
        String lastname = textLastname.getText();
                
        java.util.List<Student> wyjscie = new LinkedList<Student>();
        try{
            ResultSet resultSet = statement.executeQuery("SELECT * FROM "+tabela+" WHERE nazwisko=\'"+ lastname+"\' AND imie=\'"+name+"\'");

            int id;
            String db_name, db_lastname;
            
            while(resultSet.next()){
                id=resultSet.getInt("id");
                db_lastname = resultSet.getString("nazwisko");
                db_name = resultSet.getString("imie");
                wyjscie.add(new Student(id, db_lastname, db_name));
            }
            
        }catch(SQLException e){
            System.err.println("Problem with reading data from BD");
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
            System.err.println("Error during data entry: " + lastname_p + " " + name_p);
            exp.printStackTrace();
         }
    }
    
    public void dataBase(){
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            
            System.err.println("There is no such a driver as JDBC");
            ex.printStackTrace();
        }
        
        try {
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            createTable();
        
        } catch (SQLException ex) {
            System.err.println("Problem with connection opening");
            ex.printStackTrace();
        }
    }
    
    public void createTable(){
        String tworz = "CREATE TABLE IF NOT EXISTS STUDENCI(id INTEGER PRIMARY KEY AUTOINCREMENT, nazwisko String, imie String)";
        try {
            statement.execute(tworz);
        } catch (SQLException e) {
            System.err.println("Error during tables creating");
            e.printStackTrace();
        }

    }
    
}
