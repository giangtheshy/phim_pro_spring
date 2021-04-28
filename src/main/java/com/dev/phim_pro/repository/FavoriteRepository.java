package com.dev.phim_pro.repository;

import com.dev.phim_pro.dto.FilmStatic;
import com.dev.phim_pro.models.Favorite;
import com.dev.phim_pro.models.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    @Query(value = "SELECT film_id FROM favorite WHERE user_id=?1",nativeQuery = true)
    List<String> findAllByUserId(Long id);

    @Query(value = "SELECT f.id AS id, title,category,country,description," +
            "directors,episode,(SELECT COUNT(*) FROM favorite favo WHERE f.id=favo.film_id) AS fav_count ,comment_count,image,is_multi,stars,up_coming,url,created_date," +
            "search FROM favorite fav  JOIN film f ON fav.film_id=f.id WHERE fav.user_id=?1",nativeQuery =
            true)
    List<FilmStatic> findAllFilmByUserId(Long id);

    @Modifying
    @Query(value = "DELETE FROM favorite WHERE user_id=?1 AND film_id=?2",nativeQuery = true)
    void deleteByUidAndFid(Long uid,Long fid);


}
