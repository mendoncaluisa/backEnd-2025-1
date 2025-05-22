package edu.ifmg.produto.repository;


import edu.ifmg.produto.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //o repository não é uma classe mas é uma interface1
public interface RoleRepository extends JpaRepository<Role, Long> {


}