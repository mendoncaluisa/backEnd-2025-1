package edu.ifmg.produto.dtos;

import edu.ifmg.produto.entities.Category;
import edu.ifmg.produto.entities.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;



public class ProductDTO extends RepresentationModel<ProductDTO> { // a gente extendeu a classe RepresentationModel para o DTO ter acesso aos metodos dessa classe

    //essas descrições com a diretiva @Schema colocamos normalmante nos DTO's e não nas Entities
    @Schema(description = "Database generated ID product")
    private long id;
    @Schema(description = "Product name") //os nomes obvios nao precisamos fazer, mas vamos fazer so pra didática mesmo aqui
    private String name;
    @Schema(description = "A detailed description of the product")
    private String description;
    @Schema(description = "Product price")
    private double price;
    @Schema(description = "Product url of the image")
    private String imageUrl;
    @Schema(description = "Product categories (one or more)")
    private Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO() {}

    public ProductDTO(long id, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;


    }

    public ProductDTO(Product entity) { //estamos preenchendo os dados com uma entidade
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imageUrl = entity.getImageUrl();

        //isso faz a mesma coisa do codigo do construtor abaixo no laço forEach
        entity.getCategories().forEach(category -> this.categories.add(new CategoryDTO(category)));

    }

    public ProductDTO(Product product, Set<Category> categories) {
        this(product);

        categories
                .forEach(c -> this.categories.add(new CategoryDTO(c)));
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

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", categories=" + categories +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductDTO product)) return false;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}