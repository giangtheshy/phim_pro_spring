package com.dev.phim_pro.controllers;

import com.dev.phim_pro.models.Episode;
import com.dev.phim_pro.services.EpisodeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/episode")
public class EpisodeController {
    private final EpisodeService episodeService;

    @GetMapping("/{id}")
    public List<Episode> getAllEpisodeByFilmId(@PathVariable Long id){
        return episodeService.getAllEpisodeByFilmId(id);
    }

    @PostMapping("/create")
    public void addEpisode(@RequestBody  Episode episode){
        episodeService.addEpisode(episode);
    }
    @PutMapping("/update")
    public void updateEpisode(@RequestBody  Episode episode){
        episodeService.updateEpisode(episode);
    }
    @DeleteMapping("/delete/{id}")
    public void removeEpisode(@PathVariable Long id){
        episodeService.removeEpisode(id);
    }
}
