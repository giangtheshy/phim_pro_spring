package com.dev.phim_pro.services;

import com.dev.phim_pro.dto.FilmStatic;
import com.dev.phim_pro.exceptions.SpringPhimException;
import com.dev.phim_pro.models.Film;
import com.dev.phim_pro.models.User;
import com.dev.phim_pro.repository.FilmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class FilmService {

    private final FilmRepository filmRepository;
    private final AuthService authService;
    private final AccessAdmin accessAdmin;


    public Film createFilm(Film film){
        accessAdmin.isAccess();
        film.setComment_count(0);
        film.setFav_count(0);
        film.setCreated_date(Instant.now());
        User user = authService.getCurrentUser();
        if(!user.getRole()){
            throw new SpringPhimException("Action had been denied!");
        }
        filmRepository.save(film);
         return film;
    }
    public void deleteFilm(Long id){
        accessAdmin.isAccess();
        filmRepository.deleteFilm(id);
    }
    public List<Film> getAllFilms(){
        List<FilmStatic> filmStatics= filmRepository.findAllFilm();
        return mapToFilm(filmStatics);
    }
    public Film getFilmById(Long id){
        return filmRepository.findFilmById(id).orElseThrow(()->new SpringPhimException("Phim does not " +
                "exist"));
    }
    public Film updateFilmById(Film film){
        accessAdmin.isAccess();
         filmRepository.save(film);
         return getFilmById(film.getId());
    }
    public List<Film> mapToFilm(List<FilmStatic> list){
        ArrayList<Film> newList = new ArrayList<Film>();
        list.forEach(film->{
            newList.add(new Film(film.getID(),
                    film.getTitle(),
                    film.getCategory(),
                    film.getCountry(),
                    film.getDescription(),
                    film.getDirectors(),
                    film.getEpisode(),
                    film.getFav_count(),
                    film.getComment_count(),
                    film.getImage(),
                    film.getIs_multi(),
                    film.getStars(),
                    film.getUp_coming(),
                    film.getUrl(),
                    film.getCreated_date(),
                    film.getSearch()));
        });
        return newList;
    }
}
