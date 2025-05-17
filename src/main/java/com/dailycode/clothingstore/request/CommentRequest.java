package com.dailycode.clothingstore.request;

import com.dailycode.clothingstore.model.Product;
import com.dailycode.clothingstore.model.User;
import lombok.Data;

@Data
public class CommentRequest {
    private Long id;
    private String content;
    private Long productId;

    public CommentRequest(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
