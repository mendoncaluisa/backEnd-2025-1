package edu.ifmg.produto.entities;

import jakarta.persistence.*;


import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private double price;
    private String imageUrl;

    private Instant createAt;
    private Instant updateAt;

    @ManyToMany
    //a tabela pivô será configurada aqui no JoinTable e essas configuraçõe sao acessiveis (nao sei se tem um termo melhor pra isso) atraves de categories definido na linha abaixo
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Product() {}

    public Product(long id, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;


    }

    public Product(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();
        this.createAt = entity.getCreateAt();
        this.updateAt = entity.getUpdateAt();
    }

    public Product(Product product, Set<Category> categories) {
        this(product);
        this.categories = categories;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    @PrePersist
    public void prePersist() {
        this.createAt = Instant.now();
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updateAt = Instant.now();
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}