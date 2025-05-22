package edu.ifmg.produto.dtos;

import edu.ifmg.produto.entities.Role;
import edu.ifmg.produto.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private Long id;
    @NotBlank(message = "Campo obrigatório") //verifica se o campo veio uma string vazia da requisição
    private String firstname;
    private String lastname;
    @Email(message = "Favor, informar um e-mail válido")
    private String email;



    private Set<RoleDTO> roles = new HashSet<>();


    public UserDTO() {
    }

    public UserDTO(Long id, String firstname, String lastname, String email, String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }



    public UserDTO(User user) {
        id = user.getId();
        firstname = user.getFirstName();
        lastname = user.getLastName();
        email = user.getEmail();

        user.getRoles()
                .forEach(role ->
                        roles.add(new RoleDTO(role))
                );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}