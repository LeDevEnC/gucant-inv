package gucant.inv.models.dao;

import gucant.inv.models.data.Produit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import gucant.inv.database.SQLiteConnection;

public class ProduitDAO implements ProduitDAOInterface {
    private Connection connection;

    public ProduitDAO() {
        connection = SQLiteConnection.getInstance().getConnection();
    }

    @Override
    public void create(Produit produit) {
        String sql = "INSERT INTO Produit (name, specifications, quantite, prix, category_id, supplier_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, produit.getName());
            pstmt.setString(2, produit.getSpecifications());
            pstmt.setInt(3, produit.getQuantite());
            pstmt.setDouble(4, produit.getPrix());
            pstmt.setInt(5, produit.getCategoryId());
            pstmt.setInt(6, produit.getSupplierId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating product", e);
        }
    }

    @Override
    public Produit read(String name) {
        String sql = "SELECT * FROM Produit WHERE name = ?";
        Produit produit = null;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                produit = new Produit(
                        rs.getString("name"),
                        rs.getString("specifications"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produit;
    }

    @Override
    public void update(Produit produit) {
        String sql = "UPDATE Produit SET name = ?, specifications = ?, quantite = ?, prix = ?, category_id = ?, supplier_id = ? WHERE name = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, produit.getName());
            pstmt.setString(2, produit.getSpecifications());
            pstmt.setInt(3, produit.getQuantite());
            pstmt.setDouble(4, produit.getPrix());
            pstmt.setInt(5, produit.getCategoryId());
            pstmt.setInt(6, produit.getSupplierId());
            pstmt.setString(7, produit.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String name) {
        String sql = "DELETE FROM Produit WHERE name = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Produit> getAll() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM Produit";
        
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                produits.add(new Produit(
                        rs.getString("name"),
                        rs.getString("specifications"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    // Close connection when done
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
