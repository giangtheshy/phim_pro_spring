package com.dev.phim_pro.repository;

import com.dev.phim_pro.models.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode,Long> {
}
