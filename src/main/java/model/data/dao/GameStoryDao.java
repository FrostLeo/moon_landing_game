package model.data.dao;

import model.entity.GameStory;

import java.util.List;
import java.util.Optional;

public interface GameStoryDao {
    void save(GameStory gameStory);
//    void delete(Long id);
    void update(GameStory gameStory);
//    List<GameStory> getAll();
    Optional<GameStory> getById(Long id);
}
