package com.prameswaradev.springsecuritydemo;

import com.prameswaradev.springsecuritydemo.model.dto.AuthRequestDto;
import com.prameswaradev.springsecuritydemo.model.dto.ProductDto;
import com.prameswaradev.springsecuritydemo.model.entity.UserInfo;
import com.prameswaradev.springsecuritydemo.service.JwtService;
import com.prameswaradev.springsecuritydemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping
@RestController
public class ProductController {
    JwtService jwtService;
    AuthenticationManager authenticationManager;
    ProductService productService;
    @Autowired
    public ProductController(JwtService jwtService, AuthenticationManager authenticationManager, ProductService productService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.productService = productService;
    }


    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome! this is public endpoint";
    }

    @PostMapping("/crete")
    public ResponseEntity<?> addNewUser(@RequestBody UserInfo userInfo) {
        return productService.addUser(userInfo);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ProductDto> getAllTheProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ProductDto getProductById(@PathVariable int id) {
        return productService.getProduct(id);
    }


    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequestDto authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
