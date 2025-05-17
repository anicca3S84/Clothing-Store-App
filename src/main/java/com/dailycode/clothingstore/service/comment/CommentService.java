package com.dailycode.clothingstore.service.comment;

import com.dailycode.clothingstore.dto.CommentDto;
import com.dailycode.clothingstore.exceptions.NotFoundException;
import com.dailycode.clothingstore.model.Comment;
import com.dailycode.clothingstore.model.Product;
import com.dailycode.clothingstore.model.User;
import com.dailycode.clothingstore.repository.CommentRepository;
import com.dailycode.clothingstore.repository.ProductRepository;
import com.dailycode.clothingstore.request.CommentRequest;
import com.dailycode.clothingstore.service.product.IProductService;
import com.dailycode.clothingstore.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto addComment(CommentRequest comment) {
        User user = iUserService.getAuthenticatedUser();
        Product product = iProductService.getProductById(comment.getProductId());

        Comment newComment = new Comment();
        newComment.setProduct(product);
        newComment.setUser(user);
        newComment.setContent(comment.getContent());
        newComment.setCreateAt(LocalDateTime.now().toString());
        commentRepository.save(newComment);

        CommentDto commentDto = new CommentDto();
        commentDto.setUserId(user.getId());
        commentDto.setProductId(comment.getProductId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreateAt(LocalDateTime.now().toString());
        commentDto.setId(comment.getId());
        return commentDto;
    }

    @Override
    public CommentDto updateComment(Long id, CommentRequest comment) {
        Product product = iProductService.getProductById(comment.getProductId());
        User user = iUserService.getAuthenticatedUser();
        Long userId = getCommentById(id).getUserId();
        if(!Objects.equals(userId, user.getId())){
            throw new NotFoundException("You just can update your comment");
        }
        return Optional.ofNullable(getCommentById(id)).map(oldComment -> {
            Comment newComment = new Comment();
            newComment.setContent(comment.getContent());
            newComment.setProduct(product);
            newComment.setUser(user);
            newComment.setCreateAt(LocalDateTime.now().toString());
            commentRepository.save(newComment);

            CommentDto commentDto = new CommentDto();
            commentDto.setUserId(user.getId());
            commentDto.setProductId(comment.getProductId());
            commentDto.setContent(comment.getContent());
            commentDto.setCreateAt(LocalDateTime.now().toString());
            commentDto.setId(comment.getId());
            return commentDto;
        }).orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    @Override
    public void deleteCommentById(Long id) {
        User user = iUserService.getAuthenticatedUser();
        Long userId = getCommentById(id).getUserId();
        if(!Objects.equals(user.getId(), userId)){
            throw new NotFoundException("You just can delete your comment");
        }
        commentRepository.findById(id).ifPresentOrElse(commentRepository::delete, () -> {
            throw new NotFoundException("Comment not found");
        });
    }

    @Override
    public List<CommentDto> getAllCommentByUserId(Long userId) {
        List<Comment> list = commentRepository.findAllByUserId(userId);
        return getListCommentConvertedDto(list);
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not found"));
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setCreateAt(comment.getCreateAt());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setContent(comment.getContent());
        commentDto.setProductId(comment.getProduct().getId());
        return commentDto;
    }

    @Override
    public CommentDto convertToDto(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }

    @Override
    public List<CommentDto> getListCommentConvertedDto(List<Comment> comments) {
        return comments.stream().map(comment ->{
            CommentDto commentDto = new CommentDto();
            commentDto.setId(comment.getId());
            commentDto.setCreateAt(comment.getCreateAt());
            commentDto.setUserId(comment.getUser().getId());
            commentDto.setContent(comment.getContent());
            commentDto.setProductId(comment.getProduct().getId());
            return commentDto;
        }).toList();
    }
}
