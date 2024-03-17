package com.umami.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseManager {
    private String urlBase;
    private String host;
    private String database;
    private String userName;
    private String password;
    private static Connection connection;

    public DatabaseManager(String urlBase, String host, String database, String userName, String password) {
        // TODO Auto-generated constructor stub
        this.urlBase = urlBase;
        this.host = host;
        this.database = database;
        this.userName = userName;
        this.password = password;
    }

    public void connexion() {
        if(!isOnline()) {
            try {
                connection = DriverManager.getConnection(this.urlBase + this.host + "/" + this.database, this.userName, this.password);
                System.out.println("§a[DatabaseManager] Connection réussie. " + getConnexion());
                return;
            } catch (SQLException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }


    public Connection getConnexion() {
        return connection;
    }


    public void deconnexion() {
        if(isOnline()) {
            try {
                connection.close();
                System.out.println("§a[DatabaseManager] Déconnection réussie.");
            } catch (SQLException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public boolean isOnline() {
        try {
            if((connection == null) || (connection.isClosed()))
            {
                return false;
            }
            return true;
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    public void executeSQLScript(String script) {
        if (isOnline()) {
            try {
                Statement statement = connection.createStatement();
                statement.execute(script);
                System.out.println("[DatabaseManager] Script SQL exécuté avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("[DatabaseManager] Script SQL en échec.");
        }
    }

    public void executeSQLScriptFromFile(String filePath) {
        String scriptContent = readScriptFromFile(filePath);
        if (scriptContent != null) {
            if (!isRolesTableNotEmpty()) { // Vérifie si la table roles est vide
                try (PreparedStatement preparedStatement = connection.prepareStatement(scriptContent)) {
                    preparedStatement.execute();
                    System.out.println("[DatabaseManager] Script SQL exécuté avec succès.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isRolesTableNotEmpty() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM roles")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int rowCount = resultSet.getInt(1);
                return rowCount > 0; // Retourne true si la table roles contient des données
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true; // Par défaut, considérer la table roles comme non vide en cas d'erreur
    }

    private String readScriptFromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
