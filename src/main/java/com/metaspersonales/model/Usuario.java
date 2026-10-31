/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.metaspersonales.model;

import java.time.LocalDate;

public class Usuario {
    
    private int userId;
    private String nombres;
    private String apellidos;
    private String username;
    private String email;
    private LocalDate fechaNacimiento;
    private String telefono;

    public Usuario() {}

    public Usuario(int userId, String nombres, String apellidos,
                   String username, String email,
                   LocalDate fechaNacimiento, String telefono) {
        this.userId = userId;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.username = username;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
