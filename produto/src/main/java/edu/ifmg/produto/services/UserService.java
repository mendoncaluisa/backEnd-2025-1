package edu.ifmg.produto.services;


import edu.ifmg.produto.dtos.ProductDTO;
import edu.ifmg.produto.dtos.RoleDTO;
import edu.ifmg.produto.dtos.UserDTO;
import edu.ifmg.produto.dtos.UserInsertDTO;
import edu.ifmg.produto.entities.Product;
import edu.ifmg.produto.entities.Role;
import edu.ifmg.produto.entities.User;
import edu.ifmg.produto.projections.UserDetailsProjection;
import edu.ifmg.produto.repository.RoleRepository;
import edu.ifmg.produto.repository.UserRepository;

import edu.ifmg.produto.resources.ProductResource;
import edu.ifmg.produto.services.exceptions.DatabaseException;
import edu.ifmg.produto.services.exceptions.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {

        Page<User> list = repository.findAll(pageable);
        return list.map(u -> new UserDTO(u)); // o map gera uma nova lista onde cada elemento é um UserDTO
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {

        Optional<User> opt = repository.findById(id);
        User user = opt.orElseThrow(
                () -> new ResourceNotFound("User not found"));

        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {

        User entity = new User();
        copyDtoToEntity(dto,entity);
        entity.setPassword(
                passwordEncoder.encode(dto.getPassword()));
        User novo = repository.save(entity);

        return new UserDTO(novo);
    }

    private void copyDtoToEntity(UserDTO dto, User entity){
        entity.setFirstName(dto.getFirstname());
        entity.setLastName(dto.getLastname());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for (RoleDTO role : dto.getRoles()) {
            Role r = roleRepository.getReferenceById(role.getId()); //diferente do findbyid o getreferencebyid nao carrega relacionamentos ele é mais pra confrimar que aqueel id existe
            entity.getRoles().add(r);
        }
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto){

        try {
            User entity = repository.getReferenceById(id); //essa linha está buscando os dados no banco
            copyDtoToEntity(dto, entity);


            entity = repository.save(entity);

            return new UserDTO(entity);

        }catch (EntityNotFoundException e) {
            throw new ResourceNotFound("User not found " + id);
        }
    }

    @Transactional
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFound("User not found " + id);
        }
        try {
            repository.deleteById(id); //essa linha está buscando os dados no banco
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRoleByEmail(username);

        if(result.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = new User();
        user.setEmail(result.get(0).getUsername());
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection : result) {
            user.addRole(
                    new Role(projection.getRoleId(), projection.getAuthority())
            );
        }
        return user;
    }
}