package br.com.api.futebol.teste;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class TestConnection {
	public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/football_teams?useTimezone=true&serverTimezone=UTC";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Conexão bem-sucedida!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
