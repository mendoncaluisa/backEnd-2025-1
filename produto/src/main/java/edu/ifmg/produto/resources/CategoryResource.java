package edu.ifmg.produto.resources;

import edu.ifmg.produto.dtos.CategoryDTO;
import edu.ifmg.produto.entities.Category;
import edu.ifmg.produto.services.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController //diz que vai receber requisições
@RequestMapping(value = "/category") // category é um endpoint

public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping //quando eu usar o verbo Get em alguma requisição ele chamará o metodo findALl
    public ResponseEntity<List<CategoryDTO>> findAll(){

        //antes estávamos inserindo os dados manualmente, e agora ele chama o metodo findAll que busca
        // os dados no banco (que ainda não existe) e popula o objeto categories
        List<CategoryDTO> categories = categoryService.findAll();

        //o responseentity é um objeto de resposta que monta a resposta toda pra mim
        //body() é o método que retorna o json
        return ResponseEntity.ok().body(categories);

    }
                // passando parametro para a rota ou endpoint Ex: (value = "/{variavel})
    @GetMapping(value = "/{id}")
                                            // Avisando que o id é uma variavel na rota
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        CategoryDTO category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);

    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto) {
        dto = categoryService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {

        dto = categoryService.update(id, dto);
        return ResponseEntity.ok().body(dto);

    }

}
