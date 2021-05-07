package com.dev.phim_pro.repository;

import com.dev.phim_pro.dto.FilmStatic;
import com.dev.phim_pro.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {


    @Query(value = "SELECT f.id, title,(SELECT url FROM episode ep WHERE f.id=ep.film_id AND number_ep=1 LIMIT 1) AS url," +
            "image,stars,category,(SELECT COUNT(*) FROM episode ep WHERE f.id=ep.film_id ) AS episode,directors,country,description,up_coming,is_multi,(SELECT COUNT" +
            "(*)" +
            " FROM favorite fav WHERE f.id=fav.film_id) AS fav_count,created_date,search,(SELECT COUNT(*) FROM comment cmt WHERE f.id=cmt.film_id) AS comment_count FROM film f WHERE f.id=?1", nativeQuery = true)
    Optional<Film> findFilmById(Long id);

    @Query(value = "SELECT f.id AS id, title,category,country,description," +
            "directors,episode,(SELECT COUNT(*) FROM favorite favo WHERE f.id=favo.film_id) AS fav_count ,comment_count,image,is_multi,stars,up_coming,url,created_date," +
            "search FROM film f", nativeQuery = true)
    List<FilmStatic> findAllFilm();

    @Modifying
    @JoinColumn(foreignKey = @ForeignKey(name = "none"))
    @Query(value = "DELETE FROM film WHERE id=?1",nativeQuery = true)
    void deleteFilm(Long id);
}
