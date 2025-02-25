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
        String sql = "INSERT INTO Produit (name, specifications, quantite, PrixHT, PrixTTC, category_id, supplier_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, produit.getName());
            pstmt.setString(2, produit.getSpecifications());
            pstmt.setInt(3, produit.getQuantite());
            pstmt.setDouble(4, produit.getPrixHT());
            pstmt.setDouble(5, produit.getPrixTTC());
            pstmt.setInt(6, produit.getCategoryId());
            pstmt.setInt(7, produit.getSupplierId());
            pstmt.executeUpdate();
    
            // Récupération de l'ID généré
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                produit.setId(generatedKeys.getInt(1));  // Met à jour l'ID du produit
            }
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
                        rs.getInt("id"), // Vérifie que "id" est bien dans ta table et bien retourné
                        rs.getString("name"),
                        rs.getString("specifications"),
                        rs.getInt("quantite"),
                        rs.getDouble("prixHT"),
                        rs.getDouble("prixTTC"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produit;
    }

    @Override
    public void update(Produit produit) {
        String sql = "UPDATE Produit SET name = ?, specifications = ?, quantite = ?, PrixHT = ?, PrixTTC = ?, category_id = ?, supplier_id = ? WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, produit.getName());
            pstmt.setString(2, produit.getSpecifications());
            pstmt.setInt(3, produit.getQuantite());
            pstmt.setDouble(4, produit.getPrixHT());
            pstmt.setDouble(5, produit.getPrixTTC());
            pstmt.setInt(6, produit.getCategoryId());
            pstmt.setInt(7, produit.getSupplierId());
            pstmt.setString(8, produit.getName());
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
        String sql = """
                    SELECT p.*, c.name AS categorie_name, s.name AS supplier_name
                    FROM Produit p
                    JOIN Categorie c ON p.category_id = c.id
                    JOIN Supplier s ON p.supplier_id = s.id
                """;

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("specifications"),
                        rs.getInt("quantite"),
                        rs.getDouble("prixHT"),
                        rs.getDouble("prixTTC"),
                        rs.getInt("category_id"),
                        rs.getInt("supplier_id"));
                produit.setCategoryName(rs.getString("categorie_name")); // Correspond au vrai nom
                produit.setSupplierName(rs.getString("supplier_name"));
                produits.add(produit);
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
