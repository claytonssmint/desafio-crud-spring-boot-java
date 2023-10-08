package br.com.clayton.springboot.service;

import br.com.clayton.springboot.dto.ProductRecordDto;
import br.com.clayton.springboot.model.ProductModel;
import br.com.clayton.springboot.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void testsaveProduct() {
        ProductRecordDto productRecordDto = new ProductRecordDto("Nome do Produto", "Descrição do Produto", 10.0);
        productRecordDto.name();
        productRecordDto.description();
        productRecordDto.price();

        ProductModel savedProduct = new ProductModel();
        savedProduct.setId(1L);
        savedProduct.setName("Produto Teste");
        savedProduct.setDescription("Descrição do produto");
        savedProduct.setPrice(10.0);

        when(productRepository.save(any(ProductModel.class))).thenReturn(savedProduct);

        ProductModel result = productService.saveProduct(productRecordDto);

        verify(productRepository, times(1)).save(any(ProductModel.class));

        assertNotNull(result);
        assertEquals(savedProduct.getId(), result.getId());
        assertEquals(savedProduct.getName(), result.getName());
        assertEquals(savedProduct.getDescription(), result.getDescription());
        assertEquals(savedProduct.getPrice(), result.getPrice());

    }

    @Test
    public void testGetAllProducts() {
        List<ProductModel> productList = Arrays.asList(
                new ProductModel()
        );

        when(productRepository.findAll()).thenReturn(productList);

        List<ProductModel> result = productService.getAllProducts();

        verify(productRepository, times(1)).findAll();

        assertEquals(productList, result);

    }

    @Test
    public void testGetOneProduct() {
        long productId = 1L;

        ProductModel product = new ProductModel();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Optional<ProductModel> result = productService.getOneProduct(productId);

        verify(productRepository, times(1)).findById(productId);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    public void testDeleteProduct() {
        long productId = 1L;
        ProductModel existingProduct = new ProductModel();
        existingProduct.setId(productId); // Defina o ID do produto existente

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        boolean result = productService.deleteProduct(productId);

        assertTrue(result);

        verify(productRepository, times(1)).delete(existingProduct);
    }

    @Test
    public void testDeleteProduct_ProductNotFound() {
        long productId = 2L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        boolean result = productService.deleteProduct(productId);

        assertFalse(result);

        verify(productRepository, never()).delete(any());
    }

}


