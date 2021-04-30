package com.dev.phim_pro.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "episode")
public class Episode {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long film_id;
    private Integer number_ep;
    private String url;


}
