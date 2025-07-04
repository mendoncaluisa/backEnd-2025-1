package edu.ifmg.produto.repository;


import edu.ifmg.produto.entities.User;
import edu.ifmg.produto.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //o metodo abaixo é um metodo de consulta ao banco que busco o email o padrao findByAlguma coisa é um mét0do para buscar Alguma coisa no banco
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);

    @Query(nativeQuery = true,
            value = """
                SELECT u.email as username,
                        u.password,
                        r.id as roleId,
                        r.authority_id
                FROM tb_user u 
                INNER JOIN tb_user_role ur ON u.id = ur.user_id
                INNER JOIN tb_role r ON r.id = ur.role_id
                WHERE u.email = :email 
    """
    )
    List<UserDetailsProjection> searchUserAndRoleByEmail(String username);
}