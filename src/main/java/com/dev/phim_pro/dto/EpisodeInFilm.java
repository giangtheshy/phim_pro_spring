package com.dev.phim_pro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeInFilm {
    private String url;
    private Integer number_ep;

}
