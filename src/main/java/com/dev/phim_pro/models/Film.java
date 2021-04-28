package com.dev.phim_pro.models;

import com.dev.phim_pro.dto.EpisodeInFilm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "film")
public class Film {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private String category;
    private String country;
    private String description;
    private String directors;
    private Integer episode;
    private Integer fav_count;
    private Integer comment_count;
    private String image;
    private Boolean is_multi;
    private  Integer stars;
    private Boolean up_coming;
    private String url;
    private Instant created_date;
    private String search;

}
