package az.edu.ada.wm2.lab6.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;


@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    // Constructors
    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }


    @ManyToMany(mappedBy = "categories")
    private List<Product> products = new ArrayList<>();

    // Getters & Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}