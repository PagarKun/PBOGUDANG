/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gudang;

import java.sql.*;
import javax.swing.JOptionPane;

public class koneksi {

    public Connection connec;
    public Statement st;
    public ResultSet rs;

    public Connection setkoneksi() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connec = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventory_db",
                    "root",
                    ""
            );

            // DIBUAT SCROLLABLE: Memungkinkan ResultSet dibaca berulang kali / sebelum baris pertama
            st = connec.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null,
                    "Koneksi Gagal : " + e);

        }

        return connec;
    }
}