package com.example.ecom_proj.controller;


import com.example.ecom_proj.model.Product;
import com.example.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public ResponseEntity<?> addProduct(
            @RequestPart("product") Product product,
            @RequestPart("imageFile") MultipartFile imageFile) {
        try {
            // Directory for storing uploaded images (adjust path for your environment)
            String uploadDir = "uploads/"; // Use a safer, non-source directory
            Path uploadPath = Paths.get(uploadDir);

            // Ensure the directory exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the image file
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Set the image name in the product
            product.setImageName(fileName);

            // Save the product to the database
            Product savedProduct = service.addProduct(product);

            // Return success response
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace(); // Log the error for debugging
            return new ResponseEntity<>("Failed to upload image.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace(); // Catch any other unexpected errors
            return new ResponseEntity<>("An error occurred.", HttpStatus.BAD_REQUEST);
        }
    }

    //Created a separate url to get images
    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImagebyid(@PathVariable int productId){
        Product product = service.getproduct(productId);
        if (product != null && product.getImageName() != null) {
            String imagePath = "uploads/" + product.getImageName();
            File imageFile = new File(imagePath);

            if (imageFile.exists()) {
                try {
                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
                } catch (IOException e) {
                    return ResponseEntity.notFound().build();
                }
            }
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @RequestPart("product") Product product,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            // Fetch the existing product from the database
            Product existingProduct = service.getproduct(id);
            if (existingProduct == null) {
                return new ResponseEntity<>("Product not found.", HttpStatus.NOT_FOUND);
            }

            // If an image file is provided, replace the old image
            if (imageFile != null && !imageFile.isEmpty()) {
                // Delete the old image
                String oldImagePath = "uploads/" + existingProduct.getImageName();
                File oldImageFile = new File(oldImagePath);
                if (oldImageFile.exists()) {
                    oldImageFile.delete();
                }

                // Save the new image
                String uploadDir = "uploads/";
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                Path uploadPath = Paths.get(uploadDir);

                // Ensure the upload directory exists
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save the new image
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Update the image name in the product
                product.setImageName(fileName);
            } else {
                // Retain the existing image name if no new image is uploaded
                product.setImageName(existingProduct.getImageName());
            }

            // Update the product
            Product updatedProduct = service.updateProduct(id, product);

            // Return the updated product
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update the product.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.BAD_REQUEST);
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