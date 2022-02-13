package model.story.dao;

import model.dto.GameStoryDto;

import java.util.List;
import java.util.Optional;

public interface GameStoryDao {
    void save(GameStoryDto gameStoryDto);
//    void delete(Long id);
    void update(GameStoryDto gameStoryDto);
    List<GameStoryDto> getAll();
    Optional<GameStoryDto> getById(Long id);
}
