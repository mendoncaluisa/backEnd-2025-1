package edu.ifmg.produto.services;

import edu.ifmg.produto.dtos.ProductDTO;
import edu.ifmg.produto.entities.Product;
import edu.ifmg.produto.repository.ProductRepository;
import edu.ifmg.produto.services.exceptions.ResourceNotFound;
import edu.ifmg.produto.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


//para criar essa classe automaticamente pra mim eu vou no metodo delete (metodo que vamos testar primeiro, mas posso clicar em qualquer um- pelo que entendi) por exemplo e dentro dele damos alt + insert e aí selecionamos Test e podemos ja selecionar se queremos que crie o setup pra nos
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class) //essa classe serve pra usar uma classe mockada de Repository, ou seja, um repository fake e não o real
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock //indica que vamos mocar
    private ProductRepository productRepository; //é o repository mockado

    private long existingId;
    private long nonExistingId;
    private PageImpl<Product> page;

    //não entendi muito bem esses atributos abaixo - são constantes de teste, eu posso definir os valores que quiser, eles são uma simulação. Eu disse que o id 1 existe, e o 2 não existe. Ele não vai direto no banco entao posso colocar os valores que quiser
    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        Product product = Factory.createProduct();
        product.setId(existingId);
        page = new PageImpl<>(List.of(product));
    }

    //o service que é quem estamos testando chama o repository, e o mét0do delete chama os mét0dos existsById e deleteById , entao temos que criar o fake desses metodos
    //precisamos mockar o repository já que isso são testes unitários. Um teste nesse mét0do vai testar o mét0do delete do service, se eu usasse o repository e desse algum erro o erro poderia ser do service e do repository
    @Test
    @DisplayName("Verificando se o objeto foi deletado no BD")
    void deleteShouldDoNothingWhenIdExists() {
        //aqui simulamos o repository
        when(productRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(existingId);

        Assertions.assertDoesNotThrow(
                () -> productService.delete(existingId));

        verify(productRepository,
                times(1))
                .deleteById(existingId);
    }

    @Test
    @DisplayName("Verificando se levanta uma excessão se o objeto não existe no BD")
    void deleteShouldThrowExceptionWhenIdNonExists() {

        when(productRepository.existsById(nonExistingId)).thenReturn(false);
        //doNothing().when(productRepository).deleteById(nonExistingId);

        Assertions.assertThrows(ResourceNotFound.class,
                () -> productService.delete(nonExistingId));

        verify(productRepository,
                times(0))
                .deleteById(nonExistingId);
    }

    @Test
    @DisplayName("Verificando se o findAll retorna os dados paginados")
    void findAllShouldReturnOnePage() {

        //simulando o findAll do repository que retorna Product
        when(productRepository
                .findAll((Pageable) ArgumentMatchers.any()))
                .thenReturn(page);

        //um service retorna um DTO
        //pagina pelo que entendi são os parametros da url
        Pageable pagina = PageRequest.of(0,10);
        Page<ProductDTO> result = productService.findAll(pagina);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1,
                result.getContent().getFirst().getId());

        verify(productRepository,
                times(1)).findAll(pagina);
    }

    @Test
    @DisplayName("Verificando a busca de um produto por um ID existente")
    void findByIdShouldReturnProductWhenIdExists() {

        Product p = Factory.createProduct();
        p.setId(existingId);
        when(productRepository.findById(existingId))
                .thenReturn(Optional.of(p));

        ProductDTO dto = productService.findById(existingId);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(existingId, dto.getId());

        verify(productRepository,
                times(1)).findById(existingId);
    }
}