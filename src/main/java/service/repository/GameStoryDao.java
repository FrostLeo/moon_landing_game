package service.repository;

import model.entity.GameStory;

import java.util.Optional;

public interface GameStoryDao {
    void save(GameStory gameStory);
    void update(GameStory gameStory);
    Optional<GameStory> getById(Long id);
    //    void delete(Long id);
//    List<GameStory> getBest();
}
