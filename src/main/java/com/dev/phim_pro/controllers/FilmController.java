package com.dev.phim_pro.controllers;

import com.dev.phim_pro.exceptions.SpringPhimException;
import com.dev.phim_pro.models.Film;
import com.dev.phim_pro.services.AuthService;
import com.dev.phim_pro.services.FavoriteService;
import com.dev.phim_pro.services.FilmService;
import com.dev.phim_pro.services.WatchedService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/films")
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final WatchedService watchedService;
    private final FavoriteService favoriteService;
    private final AuthService authService;
    @PostMapping("/create")
    public ResponseEntity<Film> createFilm(@RequestBody Film film){

        return ResponseEntity
                .status(HttpStatus.OK).body(filmService.createFilm(film));

    }
    @GetMapping("/get_all")
    public ResponseEntity<List<Film> > getAllFilms(){
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getAllFilms());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getFilmById(id));
    }
    @PutMapping("/update")
    public ResponseEntity<Film> updateFilm(@RequestBody Film film){
        return ResponseEntity.status(HttpStatus.OK).body(filmService.updateFilmById(film));
    }
    @DeleteMapping("/delete/{id}")
    public void deleteFilm(@PathVariable Long id){
        filmService.deleteFilm(id);
    }
    @GetMapping("/get_fav")
    public ResponseEntity<List<Film>> getAllFilmFavorites(){

        return ResponseEntity.status(HttpStatus.OK).body(favoriteService.findAllFilmsByUserId(authService
        .getCurrentUser().getId()));
    }
    @GetMapping("/get_wat")
    public ResponseEntity<List<Film>> getAllFilmWatched(){
        return ResponseEntity.status(HttpStatus.OK).body(watchedService.findAllFilmsByUserId(authService.getCurrentUser().getId()));
    }

    @GetMapping("/add_fav/{id}")
    public void addFavorite(@PathVariable Long id){
        favoriteService.addFavorite(authService.getCurrentUser().getId(),id);
    }
    @GetMapping("/remove_fav/{id}")
    public void removeFavorite(@PathVariable Long id){
        favoriteService.removeFavorite(authService.getCurrentUser().getId(),id);
    }
    @GetMapping("/add_wat/{id}")
    public void addWatched(@PathVariable Long id){
        watchedService.addWatched(authService.getCurrentUser().getId(),id);
    }
}
