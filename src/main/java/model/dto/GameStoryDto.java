package model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import model.story.GameStory;

import javax.persistence.*;

@Entity
@Table(name = "game_story")
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameStoryDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "game_data")
    String gameData;

    public GameStoryDto() {
        gameData = new GameStory().toJson();
    }

    public GameStory toGameStory() {
        return new GameStory(gameData);
    }
}
