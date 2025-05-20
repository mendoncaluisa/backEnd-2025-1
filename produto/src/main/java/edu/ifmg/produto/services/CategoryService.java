package edu.ifmg.produto.services;

import edu.ifmg.produto.dtos.CategoryDTO;
import edu.ifmg.produto.entities.Category;
import edu.ifmg.produto.repository.CategoryRepository;
import edu.ifmg.produto.services.exceptions.DatabaseException;
import edu.ifmg.produto.services.exceptions.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired //isso é pra injeção de dependencia, e o Spring vai gerenciar o objeto categoryRepository que criei
    private CategoryRepository categoryRepository;

    //sempre vamos buscar os dados do repository e converter para DTO
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> list = categoryRepository.findAll(pageable);
        /*return list.stream().
                map(categoria-> //essa é uma função lambda (semelhante a arrow function)
                        new CategoryDTO(categoria)).
                collect(Collectors.toList());*/

        return list.map(c -> new CategoryDTO(c));

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        Optional<Category> obj =
                categoryRepository.findById(id);

        Category category = obj.orElseThrow( () -> new ResourceNotFound("Category not found " + id) );
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto){
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = categoryRepository.save(entity); //eu salvei a entity e retornei pra ele o entity salvo (que já vem com o id)
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto){

        try {
            Category entity = categoryRepository.getReferenceById(id); //o getReferenceById não busca o objeto, so a referencia dele, e como iamos alterar, nao precisava buscar os dados dele, so a referencia pra poder alterar
            entity.setName(dto.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFound("Category not found " + id);
        }

    }

    @Transactional
    public void delete(Long id){

        if(!categoryRepository.existsById(id)){
            throw new ResourceNotFound("Category not found " + id);
        }

        try {
            categoryRepository.deleteById(id);
        }catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }
}