package com.dev.phim_pro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Table;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="comment")

public class CommentResponse {
    private Long id;
    private Instant created_date;
    private String message;
    private Long film_id;
    private String username;
    private String avatar;
    private String type;
    private Long user_id;
}
