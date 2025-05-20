package edu.ifmg.produto.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ifmg.produto.dtos.ProductDTO;
import edu.ifmg.produto.services.ProductService;
import edu.ifmg.produto.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

//IT é de Integration Test
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

    //objeto que irá fazer as requisições
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2000L;

    }

    @Test
    public void fidAllShouldReturnSortedPageWhenSortByName() throws Exception {

        ResultActions result =
                mockMvc.perform( //esse objeto mockMvc é o que faz as requisições
                        get("/product?page=0&size=10&sort=name,asc")
                                .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        //o primeiro valor retornado em uma ordenação crescente por nome deve ser MachbookPro
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro")); //$.content[0].name acessa o atributo name na primeira posição do array content retornado no json
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
    }

    @Test
    public void updateShouldReturnDtoWhenIdExists() throws Exception { //o throws exception precisa ser usado quando usamos mockMvd

        ProductDTO dto = Factory.createProductDTO();
        String dtoJson = objectMapper.writeValueAsString(dto); //o objectMapper converte um dto em json (ele converte varios objetos em json - só pesquisar sobre a configuração)
        System.out.println(dtoJson);
        String nameExpected = dto.getName();
        String descriptionExpected = dto.getDescription();

        ResultActions result =
                mockMvc.perform( //esse objeto mockMvc é o que faz as requisições
                        put("/product/{id}",existingId) //o {id} é substituído pelo existingId
                                .content(dtoJson) //o update recebe um json com os dados que vou atualizar e é nesse mét0do que passamos o json
                                .contentType(MediaType.APPLICATION_JSON) //isso é o que estou mandando
                                .accept(MediaType.APPLICATION_JSON) //isso é o que estou retornando
                );

        result.andExpect(status().isOk()); //verifica se o status da requisição deu certo
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(nameExpected)); //verifica se o nome que veio na resposta da requisição é o mesmo que enviei pra atualizar (no caso enviamos um produto criado a partir de uma factory)
        result.andExpect(jsonPath("$.description").value(descriptionExpected));


    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        ProductDTO dto = Factory.createProductDTO();
        String dtoJson = objectMapper.writeValueAsString(dto);
        //System.out.println(dtoJson);


        ResultActions result =
                mockMvc.perform(
                        put("/product/{id}",nonExistingId)
                                .content(dtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isNotFound());

    }

    @Test
    public void insertShouldReturnNewObjectWhenDataIsCorrect() throws Exception { //fiz sozinha :D
        //checar se o status é 201 created
        //checar se o id do novo produto é 26
        //checar se o nome inserido é o nome esperado

        ProductDTO dto = Factory.createProductDTO();
        String dtoJson = objectMapper.writeValueAsString(dto);

        String nameExpected = dto.getName();

        ResultActions result =
                mockMvc.perform(
                        post("/product")
                                .content(dtoJson)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(existingId)); //eu mudei o existingId para 26 pra fazer esse teste porque eu sei que um novo produto teria o id 26 já que no banco temos 25 produtos
        result.andExpect(jsonPath("$.name").value(nameExpected));
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {

        ResultActions result =
                mockMvc.perform( //esse objeto mockMvc é o que faz as requisições
                        delete("/product/{id}",existingId) //o {id} é substituído pelo existingId
                );

        result.andExpect(status().isNoContent()); //verifica se o status da requisição deu certo
    }

    @Test
    public void deleteShouldReturnNoFoundWhenIdDoesNotExists() throws Exception {


        ResultActions result =
                mockMvc.perform(
                        delete("/product/{id}",nonExistingId)
                                .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isNotFound());

    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {

        ResultActions result =
                mockMvc.perform(
                        get("/product/{id}",existingId)
                                .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isOk());
        String resultJson = result.andReturn().getResponse().getContentAsString();
        System.out.println(resultJson);

        ProductDTO dto = objectMapper.readValue(resultJson, ProductDTO.class); //converteu json em DRO

        Assertions.assertEquals(existingId, dto.getId()); //verifica se o id que eu busquei é o mesmo que veio na requisição
        Assertions.assertEquals("The Lord of the Rings",dto.getName());
    }


}