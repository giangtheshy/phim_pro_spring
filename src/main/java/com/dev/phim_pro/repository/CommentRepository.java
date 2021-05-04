package com.dev.phim_pro.repository;

import com.dev.phim_pro.dto.CommentResponse;
import com.dev.phim_pro.dto.CommentStatic;
import com.dev.phim_pro.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(value = "SELECT cmt.id AS id,cmt.created_date AS created_date,message,film_id,name,avatar," +
            "type,cmt" +
            ".user_id FROM comment cmt JOIN user u ON cmt.user_id=u.id WHERE film_id=?1 ORDER BY cmt" +
            ".created_date DESC",nativeQuery = true)
    List<CommentStatic> findAllByFilmId(Long id);

    @Modifying
    @Query(value = "DELETE FROM comment WHERE user_id=?1 AND id=?2",nativeQuery = true)
    void deleteByUserIdAndId(Long uid,Long id);
}
