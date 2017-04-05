/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegenerator;

import com.mysql.jdbc.Connection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ProudSourceIT
 */
public class CodeGenerator {
    public static PrintWriter out;
    public static Connection c;
    
   public static void  makeConn() throws ClassNotFoundException {
        try {
            String url = "jdbc:mysql://192.168.1.109:3306/tickets_dev";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            Connection conn = (Connection) DriverManager.getConnection (url, "ticketdev", "j6oH9n6UxbVKYkJQ");
            c = conn;
        } catch (SQLException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    
    public static void writeToFile(String code) {
            out.println(code);       

    }

    
    public static boolean checkIfCodeIsValid(String code) {
        
        for (int i = 0; i < code.length()-1; i++) {
            if(code.charAt(i) == code.charAt(i+1)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            makeConn();
            c.setAutoCommit(false);
            out = new PrintWriter(new BufferedWriter(new FileWriter("codes.txt")));
        } catch (IOException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] input = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D", "E","F"};
    int lengthOfSinglePermutation = 5;
    
Set<String> arrValues = new HashSet<String>();
for (   String i:input){
      arrValues.add(i);
  }

    // we need to check number of unique values in array
  //Set<Integer> arrValues = new HashSet<Integer>();
//for (int i;i<input.length;i++){
//      arrValues.add(i);
//  }
    int noOfUniqueValues = arrValues.size();;
    int[] indexes = new int[lengthOfSinglePermutation];
    int totalPermutations = (int) Math.pow(noOfUniqueValues, lengthOfSinglePermutation);

    for (int i = 0; i < totalPermutations; i++) {
        String letters = "";
        for (int j = 0; j < lengthOfSinglePermutation; j++) {
            letters+=input[indexes[j]];
//            System.out.print(input[indexes[j]]);
        }
        boolean valid = checkIfCodeIsValid(letters);
        if(valid) {
            try {
                String sql = "INSERT INTO entrance_code (code) VALUES (?)";
                PreparedStatement preparedStatement = c .prepareStatement(sql);
                preparedStatement.setString(1, letters);
                preparedStatement.executeUpdate();
//                writeToFile(letters);
//           System.out.println(letters);
            } catch (SQLException ex) {
                Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       

        for (int j = 0; j < lengthOfSinglePermutation; j++) {
            if (indexes[j] >= noOfUniqueValues - 1) {
                indexes[j] = 0;
            }
            else {
                indexes[j]++;
                break;
            }
        }
    }
    out.close();
    
        try {
            c.commit();
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(CodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
     System.out.println("uspeo");
    }
    
}
