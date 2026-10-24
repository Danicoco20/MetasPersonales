package org.yourcompany.yourproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC_Example {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
String dbPass = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "password";

            try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/bd_metas_personales",
                dbUser,
                dbPass
            );

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from usuario;")){

            while (rs.next()) {
                System.out.println("Nombre de usuario: " + rs.getString("username") + ", Email: " + rs.getString("email"));
                System.out.flush();
            }
            
            

 try (Statement stmt2 = con.createStatement()){
            int filasAfectadas = stmt2.executeUpdate("INSERT INTO Área (area, user_id) VALUES ('Profesional', 1);");
            System.out.println("Filas afectadas: " + filasAfectadas);
            
            
        }
        
 try (Statement stmt3 = con.createStatement()){
           ResultSet rs3 = stmt3.executeQuery("select * from Área where user_id=1;");
            while (rs3.next()) {
                System.out.println("Area_id: " + rs3.getInt("area_id") + ", User_id: " + rs3.getInt("user_id") + ", Area: " + rs3.getString("area"));
                System.out.flush();
            
            
        }
            }
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }
}
