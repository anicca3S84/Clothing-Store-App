//package com.dailycode.clothingstore.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Data
//@Entity
//public class Comment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String content;
//    private String createAt;
//
//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    public Comment() {
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getCreateAt() {
//        return createAt;
//    }
//
//    public void setCreateAt(String createAt) {
//        this.createAt = createAt;
//    }
//
//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//}
