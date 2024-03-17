package com.umami.dao;

import com.umami.constants.Constants;
import com.umami.database.DatabaseManager;

import java.sql.*;

public class UserDao {
    private final DatabaseManager db;

    public UserDao(DatabaseManager db) {
        this.db = db;
    }

    public boolean addUser(String userId, String userName, Timestamp joinedAt) {
        try (Connection connection = db.getConnexion();
             PreparedStatement checkingStatement = connection.prepareStatement(
                     "SELECT * FROM users WHERE user_id = ?");
             PreparedStatement insertStatement = connection.prepareStatement(
                     "INSERT INTO users (user_id, user_name, joined_at, role_id) VALUES (?, ?, ?, ?)")) {

            checkingStatement.setString(1, userId);
            ResultSet resultSet = checkingStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("User already in the database.");
                return false; // User already exists
            }

            // Récupérer l'ID du rôle par défaut depuis la table roles
            int defaultRoleId = getDefaultRoleIdFromRolesTable(connection);

            insertStatement.setString(1, userId);
            insertStatement.setString(2, userName);
            insertStatement.setTimestamp(3, joinedAt);
            insertStatement.setInt(4, defaultRoleId); // Utiliser l'ID du rôle par défaut
            insertStatement.executeUpdate();
            System.out.println("User added to the database.");
            return true; // User added successfully
        } catch (SQLException e) {
            System.err.println("Error inserting user into database: " + e.getMessage());
            return false; // Failed to add user
        }
    }

    private int getDefaultRoleIdFromRolesTable(Connection connection) throws SQLException {
        int defaultRoleId = 1; // Valeur par défaut de l'ID du rôle
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id FROM roles WHERE role_name = ?")) {
            statement.setString(1, Constants.DEFAULT_ROLE_NAME_ON_JOIN);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                defaultRoleId = resultSet.getInt("id");
            }
        }
        return defaultRoleId;
    }

}
