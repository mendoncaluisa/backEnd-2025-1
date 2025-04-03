package edu.ifmg.produto.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity //diz que Category é um "model" é uma tabela no banco
//@Table(name= "tb_category") é a diretiva pra nomear a tabela que não terá o mesmo nome da classe

public class Category { //logo a tabela será categories

    @Id //sempre importar do jakarta
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;

    public Category(Long id, String nome) {
        this.id = id;
        this.name = nome;
    }

    public Category() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", nome='" + name + '\'' +
                '}';
    }

}
