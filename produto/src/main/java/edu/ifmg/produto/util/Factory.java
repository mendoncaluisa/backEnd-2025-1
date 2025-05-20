package edu.ifmg.produto.util;

import edu.ifmg.produto.dtos.ProductDTO;
import edu.ifmg.produto.entities.Category;
import edu.ifmg.produto.entities.Product;

public class Factory {

    public static Product createProduct() {
        Product p = new Product();
        p.setName("IPhone XXX");
        p.setPrice(5000);
        p.setImageUrl("http://img.com/iphonexxx.jpg");
        p.getCategories()
                .add(new Category(1L,"News"));
        return p;
    }

    //retorna um product DTO
    public static ProductDTO createProductDTO(){
        Product p = createProduct();

        return new ProductDTO(p);
    }
}
