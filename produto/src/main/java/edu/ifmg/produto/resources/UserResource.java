package edu.ifmg.produto.resources;



import edu.ifmg.produto.dtos.UserDTO;
import edu.ifmg.produto.dtos.UserInsertDTO;

import edu.ifmg.produto.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Controller/Resource for Users")
public class UserResource {

    @Autowired
    private UserService userService;

    //apenas adicionar o Pageable como parametro já exclui a necessidade de adicionar os @RequestParam porque ele já tem tudo pronto
    @GetMapping(produces = "application/json")
    @Operation(
            description = "Get all users",
            summary = "Get all users",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200")
            }
    )
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
        Page<UserDTO> users = userService.findAll(pageable);
        return ResponseEntity.ok(users); //é a mesma coisa que ok().body(users);
    }

    @GetMapping (value = "/{id}", produces = "application/json")
    @Operation(
            description = "Get a user",
            summary = "Get a user",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(user); //é a mesma coisa que ok().body(products);
    }

    @PostMapping(produces = "application/json")
    @Operation(
            description = "Create a new user",
            summary = "Create a new user",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
            }
    )
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) { //o @Valid serve pra aplicar as validaçoes que adicionamos no DTO (@Size, @Positive, ect.)
        UserDTO user = userService.insert(dto);

        //essa uri que retornamos já está atendendo ao HATEOAS - eu insiro um produto e ja digo pro cara o que eu posso fazer com aquele produto, e nesse caço ele foi no cabeçalho da resposta
        URI uri = ServletUriComponentsBuilder.
                fromCurrentRequest(). //pega o caminho da minha aplicação
                        path("/{id}").//ele adiciona o id na rota
                        buildAndExpand(user.getId()).
                toUri();

        return ResponseEntity.created(uri).body(user);
    }


    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description = "Update a user",
            summary = "Update a user",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    public ResponseEntity<UserDTO> update(@Valid @PathVariable Long id, @RequestBody UserDTO dto) {
        dto = userService.update(id,dto);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value="/{id}")
    @Operation(
            description = "Delete a user",
            summary = "Delete a user",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }





}