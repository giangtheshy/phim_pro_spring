package com.dev.phim_pro.repository;

import com.dev.phim_pro.models.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode,Long> {

    @Query(value = "SELECT * FROM episode WHERE film_id=?1",nativeQuery = true)
    List<Episode> findAllByFilm_Id(Long id);
}
