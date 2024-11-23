package com.example.ecom_proj.controller;


import com.example.ecom_proj.model.Product;
import com.example.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String greet(){
        return "Hello Java";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
//         Response entity is to send the http status with the response
        return new ResponseEntity<>( service.getAllProducts(), HttpStatus.OK);

    }



    @GetMapping("/product/{prdid}")
    public  ResponseEntity<Product> getproduct (@PathVariable int prdid)

    {

        return new ResponseEntity<>(service.getproduct(prdid), HttpStatus.OK);

    }

    @PostMapping("/product")

    public  ResponseEntity<?> addproduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        try {
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1,HttpStatus.CREATED);
        }
        catch (Exception e){
   return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//Created a separate url to get images
    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImagebyid(@PathVariable int productId){
        Product product = service.getproduct(productId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);

    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateproduct(@PathVariable int id, @RequestPart Product product, @RequestPart MultipartFile imageFile){
        Product product1 = null;
        try {
            product1 = service.updateProduct(id,product,imageFile);
        } catch (IOException e) {

                return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
        if(product1!=null){
           return new ResponseEntity<>("updateed",HttpStatus.OK);

       }else{
           return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
       }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteproduct(@PathVariable int id){
        Product prod = service.getproduct(id);
        if(prod!=null){
            service.deleteproduct(id);
            return new ResponseEntity<>("Deleted Sucessfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products = service.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

}