package gucant.inv.models.dao;

import gucant.inv.database.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private Connection connection;

    public SupplierDAO() {
        connection = SQLiteConnection.getInstance().getConnection();
    }

    public List<String> getAllSuppliers() {
        List<String> suppliers = new ArrayList<>();
        String query = "SELECT name FROM Supplier";  // Assure-toi que le nom de la table est correct

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                suppliers.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }
}