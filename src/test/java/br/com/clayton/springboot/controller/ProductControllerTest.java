package br.com.clayton.springboot.controller;

import br.com.clayton.springboot.dto.ProductRecordDto;
import br.com.clayton.springboot.model.ProductModel;
import br.com.clayton.springboot.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public  void getAllProducts() throws Exception {
        var product = new ProductModel();
        product.setId(1L);
        product.setName("Produto 1");
        product.setDescription("Descrição do Produto 1");
        product.setPrice(10.0);

        var product2 = new ProductModel();
        product2.setId(2L);
        product2.setName("Produto 2");
        product2.setDescription("Descrição do Produto 2");
        product2.setPrice(15.0);

        when(productService.getAllProducts()).thenReturn(Arrays.asList(product, product2));

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Produto 1"))
                .andExpect(jsonPath("$[0].description").value("Descrição do Produto 1"))
                .andExpect(jsonPath("$[0].price").value(10.0))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Produto 2"))
                .andExpect(jsonPath("$[1].description").value("Descrição do Produto 2"))
                .andExpect(jsonPath("$[1].price").value(15.0));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testSaveProduct() throws Exception {
        String jsonProductDto = "{\"name\": \"Produto Teste\", \"description\": \"Descrição do produto\", \"price\": 10.0}";

        ProductModel savedProduct = new ProductModel();
        savedProduct.setId(1L);
        savedProduct.setName("Produto Teste");
        savedProduct.setDescription("Descrição do produto");
        savedProduct.setPrice(10.0);

        when(productService.saveProduct(any(ProductRecordDto.class))).thenReturn(savedProduct);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProductDto))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Produto Teste"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Descrição do produto"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10.0));

        verify(productService, times(1)).saveProduct(any(ProductRecordDto.class));

    }
    @Test
    public void testGetOneProduct() throws Exception {

        long productId = 1L;

        ProductModel product = new ProductModel();
        product.setId(productId);
        product.setName("Produto 1");
        product.setDescription("Descrição do Produto 1");
        product.setPrice(10.0);

        when(productService.getOneProduct(productId)).thenReturn(Optional.of(product));

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Produto 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Descrição do Produto 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10.0));

        verify(productService, times(1)).getOneProduct(productId);

    }

    @Test
    public void testUpdateProduct() throws Exception {

        long productId = 1L;

        String jsonUpdatedProductDto = "{\"name\": \"Produto Atualizado\", \"description\": \"Nova descrição do produto\", \"price\": 15.0}";

        ProductModel existingProduct = new ProductModel();
        existingProduct.setId(productId);
        existingProduct.setName("Produto Existente");
        existingProduct.setDescription("Descrição do Produto Existente");
        existingProduct.setPrice(10.0);

        ProductModel updatedProduct = new ProductModel();
        updatedProduct.setId(productId);
        updatedProduct.setName("Produto Atualizado");
        updatedProduct.setDescription("Nova descrição do produto");
        updatedProduct.setPrice(15.0);

        when(productService.getOneProduct(productId)).thenReturn(Optional.of(existingProduct));

        when(productService.updateProduct(eq(productId), any(ProductRecordDto.class))).thenReturn(Optional.of(updatedProduct));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdatedProductDto))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Produto Atualizado"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Nova descrição do produto"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(15.0));


        verify(productService, times(1)).updateProduct(eq(productId), any(ProductRecordDto.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {

        long productId = 1L;

        when(productService.deleteProduct(productId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Product deleted successfully"));

        verify(productService, times(1)).deleteProduct(productId);

    }
}











