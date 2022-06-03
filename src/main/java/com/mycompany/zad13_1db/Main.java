package com.mycompany.zad13_1db;

import java.awt.Dimension;
import javax.swing.JFrame;


    public class Main {
        
        public static void main(String[] args) {
            
            BazaDanych bazaDanych = new BazaDanych();

            JFrame frame = new JFrame("Data base");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //Panel panel1 = new Panel();
            frame.getContentPane().add(new Panel());
            
            
            
            frame.setPreferredSize(new Dimension(500,400));
            frame.pack();
            frame.setVisible(true);
            
            bazaDanych.zamknijPolaczenie();
        }
}

