package com.umami.dao;

import com.umami.constants.Constants;
import com.umami.database.DatabaseManager;

import java.sql.*;

public class RoleDao {
    private final DatabaseManager db;

    public RoleDao(DatabaseManager db) {
        this.db = db;
    }
}
