package gucant.inv.models.dao;

import gucant.inv.models.data.Produit;
import java.util.List;

public interface ProduitDAOInterface {
    void create(Produit produit);  // Insert a new product
    Produit read(String name);  // Read a product by name
    void update(Produit produit);  // Update an existing product
    void delete(String name);  // Delete a product by name
    List<Produit> getAll();  // Get all products
}
