package edu.ifmg.produto.resources;

import edu.ifmg.produto.dtos.CategoryDTO;
import edu.ifmg.produto.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController //diz que vai receber requisições
@RequestMapping(value = "/category") // category é um endpoint

public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping //quando eu usar o verbo Get em alguma requisição ele chamará o metodo findALl
    public ResponseEntity<Page<CategoryDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "direction",defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy",defaultValue = "id") String orderBy){ //request param uma vez que os parametros nao sao obrigatorios (PathVariable deixa obrigatorio)

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        //antes estávamos inserindo os dados manualmente, e agora ele chama o metodo findAll que busca os dados no banco (que ainda não existe) e popula o objeto categories
        Page<CategoryDTO> categories = categoryService.findAll(pageable);

        //o responseentity é um objeto de resposta que monta a resposta toda pra mim
        //body() é o méto do que retorna o json
        return ResponseEntity.ok().body(categories);

    }

    //a rota que acessa esse méto do é o category/id
    @GetMapping (value = "/{id}")//quando eu usar o verbo Get em alguma requisição ele chamará o metodo findALl
    public ResponseEntity<CategoryDTO>
    findById(@PathVariable Long id){ //passa o id que vem da requisição aqui, o PathVariable diz que receberá uma variavel de uma requisição

        //antes estávamos inserindo os dados manualmente, e agora ele chama o metodo findAll que busca os dados no banco (que ainda não existe) e popula o objeto categories
        CategoryDTO category = categoryService.findById(id);

        //o responseentity é um objeto de resposta que monta a resposta toda pra mim
        //body() é o méto do que retorna o json
        return ResponseEntity.ok().body(category);

    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){

        dto = categoryService.insert(dto);

        //abaixo criamos a uri com o endpoind que acessará esse recurso (o último nível do REST) e partir dessa rota tem varias acçoes que posso fazer com o category retornado (editar, excluir e etc)
        //quando eu crio uma category, o que mais eu posso fazer? eu posso visualizá-la, entao por isso retornamos a uri que me permite ver a categoria
        URI uri = ServletUriComponentsBuilder.
                fromCurrentRequest(). //pega o caminho da minha aplicação
                        path("/{id}"). //ele adiciona o id na rota
                        buildAndExpand(dto.getId()).
                toUri();

        return ResponseEntity.created(uri).body(dto);

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto){

        dto = categoryService.update(id, dto);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}