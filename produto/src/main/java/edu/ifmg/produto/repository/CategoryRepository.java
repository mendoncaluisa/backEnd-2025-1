package edu.ifmg.produto.repository;


import edu.ifmg.produto.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //digo para o Spring que essa classe é um Repository no entano para criar um repository, nao usamos classes mas interface
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
