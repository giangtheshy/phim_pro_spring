package com.dev.phim_pro.services;

import com.dev.phim_pro.dto.FilmStatic;
import com.dev.phim_pro.models.Favorite;
import com.dev.phim_pro.models.Film;
import com.dev.phim_pro.repository.FavoriteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final FilmService filmService;

    public List<Film> findAllFilmsByUserId(Long id){
        List<FilmStatic> filmStatics= favoriteRepository.findAllFilmByUserId(id);
        return filmService.mapToFilm(filmStatics);
    }

    public void addFavorite(Long uid,Long fid){
        Favorite fav = new Favorite(uid,fid);

        Favorite newFav = favoriteRepository.save(fav);

    }
    public void removeFavorite(Long uid,Long fid){
        favoriteRepository.deleteByUidAndFid(uid,fid);
    }
}
