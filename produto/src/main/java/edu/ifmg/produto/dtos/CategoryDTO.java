package edu.ifmg.produto.dtos;
import edu.ifmg.produto.entities.Category;

/*
* O que é DTO?
* Permite uma busca de dados mais segura. Trafégo de dados não é mais feito
* usando as entidades e sim as categorias. Dessa forma é possível mandar apenas as
* informações desejadas.
* */
public class CategoryDTO {
    private Long id;
    private String name;

    public CategoryDTO() {

    }

    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
