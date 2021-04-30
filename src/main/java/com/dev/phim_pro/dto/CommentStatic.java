package com.dev.phim_pro.dto;

import java.time.Instant;

public interface CommentStatic {
     Long getID();
     Instant getCreated_date();
     String getMessage();
     Long getFilm_id();
     String getName();
     String getAvatar();
     Long getUser_id();
}
