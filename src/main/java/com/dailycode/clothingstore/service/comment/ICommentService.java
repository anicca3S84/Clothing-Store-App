package com.dailycode.clothingstore.service.comment;

import com.dailycode.clothingstore.dto.CommentDto;
import com.dailycode.clothingstore.model.Comment;
import com.dailycode.clothingstore.model.User;
import com.dailycode.clothingstore.request.CommentRequest;

import java.util.List;

public interface ICommentService {
    CommentDto addComment(CommentRequest comment);
    CommentDto updateComment(Long id, CommentRequest comment);
    void deleteCommentById(Long id);
    List<CommentDto> getAllCommentByUserId(Long userId);
    CommentDto getCommentById(Long id);
    CommentDto convertToDto(Comment comment);
    List<CommentDto> getListCommentConvertedDto(List<Comment> comments);
}
