package com.dev.phim_pro.services;

import com.dev.phim_pro.models.Episode;
import com.dev.phim_pro.repository.EpisodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final AccessAdmin accessAdmin;

    public List<Episode> getAllEpisodeByFilmId(Long id){
        List<Episode> episodes = episodeRepository.findAllByFilm_Id(id);
        return episodes;
    }

    public void addEpisode(Episode episode){
        accessAdmin.isAccess();
        episodeRepository.save(episode);
    }
    public void removeEpisode(Long id){
        accessAdmin.isAccess();
        episodeRepository.deleteById(id);
    }
    public void updateEpisode(Episode episode){
        accessAdmin.isAccess();
        episodeRepository.save(episode);
    }

}
