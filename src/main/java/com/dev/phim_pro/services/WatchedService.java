package com.dev.phim_pro.services;

import com.dev.phim_pro.dto.FilmStatic;
import com.dev.phim_pro.models.Film;
import com.dev.phim_pro.models.Watched;
import com.dev.phim_pro.repository.WatchedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class WatchedService {

    private final WatchedRepository watchedRepository;
    private final FilmService filmService;


    public List<Film> findAllFilmsByUserId(Long id){

        List<FilmStatic> filmStatics= watchedRepository.findAllFilmByUserId(id);
       return filmService.mapToFilm(filmStatics);
    }
    public void addWatched(Long uid,Long fid){
        Watched newWat = new Watched(uid,fid);

        watchedRepository.save(newWat);
    }
}
