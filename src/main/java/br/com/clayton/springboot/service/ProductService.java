package br.com.clayton.springboot.service;

import br.com.clayton.springboot.dto.ProductRecordDto;
import br.com.clayton.springboot.model.ProductModel;
import br.com.clayton.springboot.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductModel saveProduct(ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return productRepository.save(productModel);
    }

    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductModel> getOneProduct(long id) {
        return productRepository.findById(id);
    }

    public Optional<ProductModel> updateProduct(long id, ProductRecordDto productRecordDto) {
        Optional<ProductModel> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            var productModel = optionalProduct.get();
            BeanUtils.copyProperties(productRecordDto, productModel);
            return Optional.of(productRepository.save(productModel));
        }
        return Optional.empty();
    }

    public boolean deleteProduct(long id) {
        Optional<ProductModel> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
            return true;
        }
        return false;
    }

}
