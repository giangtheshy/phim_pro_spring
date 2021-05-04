package com.dev.phim_pro.services;

import com.dev.phim_pro.dto.CommentResponse;
import com.dev.phim_pro.dto.CommentStatic;
import com.dev.phim_pro.exceptions.SpringPhimException;
import com.dev.phim_pro.models.Comment;
import com.dev.phim_pro.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final AuthService authService;

    public Comment createComment(Comment comment){
        comment.setUser_id(authService.getCurrentUser().getId());
        comment.setCreated_date(Instant.now());
        return commentRepository.save(comment);
    }
    public List<CommentResponse> getCommentByFilmId(Long id){
        List<CommentStatic> cmtStatics =  commentRepository.findAllByFilmId(id);
        ArrayList<CommentResponse> arrayList = new ArrayList<CommentResponse>();
        cmtStatics.forEach(cmtStatic->{
            arrayList.add(mapToComment(cmtStatic));
        });
        return arrayList;
    }
    public void updateComment(Comment comment){
        if(!authService.getCurrentUser().getId().equals(comment.getUser_id())){
            throw new SpringPhimException("Action had been denied!");
        }
        commentRepository.save(comment);
    }
    public void deleteComment(Long id){
        commentRepository.deleteByUserIdAndId(authService.getCurrentUser().getId(),id);
    }
    public CommentResponse mapToComment(CommentStatic commentStatic){
        CommentResponse comment = new CommentResponse();
        comment.setId(commentStatic.getID());
        comment.setCreated_date(commentStatic.getCreated_date());
        comment.setFilm_id(commentStatic.getFilm_id());
        comment.setMessage(commentStatic.getMessage());
        comment.setUsername(commentStatic.getName());
        comment.setAvatar(commentStatic.getAvatar());
        comment.setUser_id(commentStatic.getUser_id());
        comment.setType(commentStatic.getType());
    return comment;
    }
}
