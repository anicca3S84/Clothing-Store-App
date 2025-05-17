package com.dailycode.clothingstore.controller;


import com.dailycode.clothingstore.dto.CommentDto;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Comment;
import com.dailycode.clothingstore.model.User;
import com.dailycode.clothingstore.request.CommentRequest;
import com.dailycode.clothingstore.response.ApiResponse;
import com.dailycode.clothingstore.service.comment.ICommentService;
import com.dailycode.clothingstore.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/comments/")
public class CommentController {
    @Autowired
    private ICommentService iCommentService;
    @Autowired
    private IUserService iUserService;

    @PostMapping("comment/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> addComment(@RequestBody CommentRequest request) {
        try {
            CommentDto comment = iCommentService.addComment(request);
            return ResponseEntity.ok(new ApiResponse("Add success", comment));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("comment/{id}/update")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> updateComment(@RequestBody CommentRequest request, @PathVariable Long id) {
        try {
            CommentDto comment = iCommentService.updateComment(id, request);
            return ResponseEntity.ok(new ApiResponse("Update success", comment));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("comment/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long id) {
        try {
            iCommentService.deleteCommentById(id);
            return ResponseEntity.ok(new ApiResponse("Delete success", null));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("by/{id}/comment")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getCommentById(@PathVariable Long id) {
        try {
            CommentDto comment = iCommentService.getCommentById(id);
            return ResponseEntity.ok(new ApiResponse("Get success", comment));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("by/user/{userId}/comment")
    public ResponseEntity<ApiResponse> getCommentsByUserId(@PathVariable Long userId) {
        try {
            List<CommentDto> comments = iCommentService.getAllCommentByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Get success", comments));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
