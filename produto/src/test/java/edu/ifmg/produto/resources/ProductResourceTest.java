package edu.ifmg.produto.resources;

import edu.ifmg.produto.dtos.ProductDTO;
import edu.ifmg.produto.services.ProductService;
import edu.ifmg.produto.util.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(ProductResource.class)

//no Resource que recebemos as requisições

class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc; //responsável pelas requisições que desejo testar

    //camada que quero mocar
    @MockitoBean
    private ProductService productService;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2000L;
        productDTO = Factory.createProductDTO();
        productDTO.setId(1);
        page = new PageImpl<>(List.of(productDTO));
    }

    @Test
    void findAllShouldReturnAllPage() throws Exception {

        //criar o mét0dod mocado
        when(productService.findAll(any()))
                .thenReturn(page);

        //teste a requisição
        ResultActions result =
                mockMvc.perform(
                        get("/product")
                                .accept("application/json")
                );

        //Analisa o resultado
        result.andExpect(status().isOk());
    }

    @Test
    void findByIdShouldReturnProductWhenIdExists() throws Exception {

        //criar o mét0dod mocado
        when(productService.findById(existingId))
                .thenReturn(productDTO);

        //teste a requisição
        ResultActions result =
                mockMvc.perform(
                        get("/product{id}", existingId)
                                .accept("application/json")
                );

        //Analisa o resultado
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
    }
}