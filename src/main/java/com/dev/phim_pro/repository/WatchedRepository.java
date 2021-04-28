package com.dev.phim_pro.repository;

import com.dev.phim_pro.dto.FilmStatic;
import com.dev.phim_pro.models.Film;
import com.dev.phim_pro.models.Watched;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchedRepository extends JpaRepository<Watched,Long> {

    @Query(value = "SELECT film_id FROM watched WHERE user_id=?1",nativeQuery = true)
    List<String> findAllByUserId(Long id);

    @Query(value = "SELECT f.id AS id, title,category,country,description," +
            "directors,episode,(SELECT COUNT(*) FROM favorite favo WHERE f.id=favo.film_id) AS fav_count ,comment_count,image,is_multi,stars,up_coming,url,created_date," +
            "search FROM watched w  JOIN film f ON w.film_id=f.id WHERE w.user_id=?1",nativeQuery = true)
    List<FilmStatic> findAllFilmByUserId(Long id);
}
