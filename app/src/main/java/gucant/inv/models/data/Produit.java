package gucant.inv.models.data;

public class Produit {
    private String name;
    private String specifications;
    private int quantite;
    private double prix;
    private int categoryId;
    private int supplierId;

    public Produit(String name, String specifications, int quantite, double prix, int categoryId, int supplierId) {
        setName(name);
        setSpecifications(specifications);
        setQuantite(quantite);
        setPrix(prix);
        setCategoryId(categoryId);
        setSupplierId(supplierId);
    }

    public String getName() {
        return name;
    }

    public String getSpecifications() {
        return specifications;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrix() {
        return prix;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setSpecifications(String specifications) {
        if (specifications == null || specifications.isEmpty()) {
            throw new IllegalArgumentException("Specifications cannot be null or empty");
        }
        this.specifications = specifications;
    }

    public void setQuantite(int quantite) {
        if (quantite < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantite = quantite;
    }

    public void setPrix(double prix) {
        if (prix < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.prix = prix;
    }

    public void setCategoryId(int categoryId) {
        if (categoryId < 0) {
            throw new IllegalArgumentException("Category ID cannot be negative");
        }
        this.categoryId = categoryId;
    }

    public void setSupplierId(int supplierId) {
        if (supplierId < 0) {
            throw new IllegalArgumentException("Supplier ID cannot be negative");
        }
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        return "Produit{" +
            "name='" + name + '\'' +
            ", specifications='" + specifications + '\'' +
            ", quantite=" + quantite +
            ", prix=" + prix +
            ", categoryId=" + categoryId +
            ", supplierId=" + supplierId +
            '}';
    }
}
