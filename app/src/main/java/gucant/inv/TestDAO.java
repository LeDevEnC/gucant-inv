package gucant.inv;

import gucant.inv.models.dao.ProduitDAO;
import gucant.inv.models.data.Produit;

public class TestDAO {
    public static void main(String[] args) {
        ProduitDAO produitDAO = new ProduitDAO();
        Produit produit = new Produit("Laptop", "8GB RAM, 256GB SSD", 10, 500.0, 1, 1);
        produitDAO.create(produit);
        System.out.println(produitDAO.read("Laptop").toString());
        produit.setQuantite(20);
        produitDAO.update(produit);
        System.out.println(produitDAO.read("Laptop").toString());
        produitDAO.delete("Laptop");
        System.out.println(produitDAO.getAll());
    }
}
