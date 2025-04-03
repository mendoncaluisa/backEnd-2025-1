package edu.ifmg.produto.services;

import edu.ifmg.produto.dtos.CategoryDTO;
import edu.ifmg.produto.entities.Category;
import edu.ifmg.produto.repository.CategoryRepository;
import edu.ifmg.produto.services.exceptions.ResourceNotFound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired //isso é pra injeção de dependencia, e o Spring vai gerenciar o objeto categoryRepository que criei
    private CategoryRepository categoryRepository;

    /*
    * Busca os dados de DTO
    * mapeando cada posição do objeto no banco de dados
    * */
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = categoryRepository.findAll();
        return list.stream(). // funções lambdas - conceito de programação funcional
                map(categoria-> new CategoryDTO(categoria)).collect(Collectors.toList()); //cria um objeto anonimo
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        //tipo Optional é um objeto que não permite o objeto dentro <> nunca ser null
        Optional<Category> obj = categoryRepository.findById(id);
        Category category = obj.orElseThrow(() -> new ResourceNotFound("Categoy not found " + id));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.sameName(dto.getName());
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try { 
            Category entity = categoryRepository.getReferenceById(id);
            entity.setName(dto.getName());
            entity.categoryRepository.save(entity);
            return new CategoryDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFound("Category not found " + id);
        }
        
    }

}
