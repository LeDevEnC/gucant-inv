package gucant.inv.models.data;

public class Produit {
    private String name;
    private String specifications;
    private int quantite;
    private double prix;
    private int categoryId;
    private int supplierId;
    private String categoryName;
    private String supplierName;
    private int id;

    // Constructeur avec id
    public Produit(int id, String name, String specifications, int quantite, double prix, int categoryId,
            int supplierId) {
        setId(id); // Ici, tu peux conserver le setId() pour initialiser l'ID
        setName(name);
        setSpecifications(specifications);
        setQuantite(quantite);
        setPrix(prix);
        setCategoryId(categoryId);
        setSupplierId(supplierId);
    }

    // Constructeur sans id (l'ID sera attribué plus tard)
    public Produit(String name, String specifications, int quantite, double prix, int categoryId, int supplierId) {
        setName(name);
        setSpecifications(specifications);
        setQuantite(quantite);
        setPrix(prix);
        setCategoryId(categoryId);
        setSupplierId(supplierId);
        this.id = 0; // L'ID est nul, il sera attribué après l'insertion dans la base de données
    }

    public int getId() {
        return id;
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

    public String getCategoryName() {
        return categoryName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setId(int id) {
        if (id == 0) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }
        this.id = id;
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

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
                ", categoryName='" + categoryName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                '}';
    }
}
