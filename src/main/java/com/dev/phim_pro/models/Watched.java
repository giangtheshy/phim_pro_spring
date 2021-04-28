package com.dev.phim_pro.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "watched")
public class Watched {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long user_id;
    private Long film_id;

    public Watched(Long uid,Long fid){
        user_id=uid;
        film_id=fid;
    }

}
