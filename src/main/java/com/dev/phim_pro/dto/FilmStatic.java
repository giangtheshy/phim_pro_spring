package com.dev.phim_pro.dto;


import java.time.Instant;
import java.util.List;

public interface FilmStatic {

     Long getID();
     String getTitle();
     String getCategory();
     String getCountry();
     String getDescription();
     String getDirectors();
     Integer getEpisode();
     Integer getFav_count();
     Integer getComment_count();
     String getImage();
     Boolean getIs_multi();
      Integer getStars();
     Boolean getUp_coming();
     String getUrl();
     Instant getCreated_date();
     String getSearch();
}
