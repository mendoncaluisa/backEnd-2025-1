package edu.ifmg.produto.services;

import edu.ifmg.produto.dtos.CategoryDTO;
import edu.ifmg.produto.dtos.ProductDTO;
import edu.ifmg.produto.entities.Category;
import edu.ifmg.produto.entities.Product;
import edu.ifmg.produto.repository.ProductRepository;
import edu.ifmg.produto.resources.ProductResource;
import edu.ifmg.produto.services.exceptions.DatabaseException;
import edu.ifmg.produto.services.exceptions.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){

        Page<Product> list =
                productRepository.findAll(pageable);
        return list.map(product ->
                new ProductDTO(product)
                        .add(linkTo(methodOn(ProductResource.class).findAll(null)).withSelfRel()) //essa linha cria um link para o méto do findAll da classe ProductResource
                        .add(linkTo(methodOn(ProductResource.class).findById(product.getId())).withRel("Get a product"))

        );

    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){

        Optional<Product> obj = productRepository.findById(id);

        Product product = obj.orElseThrow(()->
                new ResourceNotFound("Product not found " + id));

        return new ProductDTO(product)

                .add( linkTo( methodOn(ProductResource.class).findById(product.getId() ) ).withSelfRel() )
                .add( linkTo( methodOn(ProductResource.class).findAll(null) ) .withRel("All products") )
                .add( linkTo( methodOn(ProductResource.class).delete(product.getId())).withRel("Delete product") )
                .add( linkTo( methodOn(ProductResource.class).update(product.getId(), new ProductDTO(product))).withRel("Update product") )

                ;



    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product entity = new Product();
        /*//preciso preencher todos os campos um a um
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImageUrl(dto.getImageUrl());
        */

        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);

        return new ProductDTO(entity)
                .add( linkTo( methodOn(ProductResource.class).findById(entity.getId() ) ).withRel("Get a product") )
                .add( linkTo( methodOn(ProductResource.class).findAll(null) ) .withRel("All products") )
                .add( linkTo( methodOn(ProductResource.class).delete(entity.getId())).withRel("Delete product") )
                .add( linkTo( methodOn(ProductResource.class).update(entity.getId(), new ProductDTO(entity))).withRel("Update product") )

                ;
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto){

        try {
            Product entity = productRepository.getReferenceById(id); //essa linha está buscando os dados no banco
            copyDtoToEntity(dto, entity);


            entity = productRepository.save(entity);

            return new ProductDTO(entity)
                    .add( linkTo( methodOn(ProductResource.class).findById(entity.getId() ) ).withRel("Update a product") )
                    .add( linkTo( methodOn(ProductResource.class).findAll(null) ) .withRel("All products") )
                    .add( linkTo( methodOn(ProductResource.class).delete(entity.getId())).withRel("Delete product") )


                    ;

        }catch (EntityNotFoundException e) {
            throw new ResourceNotFound("Product not found " + id);
        }


    }

    @Transactional
    public void delete(Long id){

        if(!productRepository.existsById(id)){
            throw new ResourceNotFound("Product not found " + id);
        }
        try {
            productRepository.deleteById(id); //essa linha está buscando os dados no banco

        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }


    }


    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImageUrl(dto.getImageUrl());

        dto.getCategories()
                .forEach(c ->
                        entity.getCategories().add(new Category(c)));
    }

}