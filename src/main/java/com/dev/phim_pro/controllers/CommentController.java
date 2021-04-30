package com.dev.phim_pro.controllers;

import com.dev.phim_pro.dto.CommentResponse;
import com.dev.phim_pro.models.Comment;
import com.dev.phim_pro.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/comments")
@Transactional
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public Comment createComment(@RequestBody Comment comment){
        return commentService.createComment(comment);
    }
    @GetMapping("/{id}")
    public List<CommentResponse> getAllCommentByFilmId(@PathVariable Long id){
        return commentService.getCommentByFilmId(id);
    }
    @PutMapping("/update")
    public void updateComment(@RequestBody Comment comment){
        commentService.updateComment(comment);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
    }
}
