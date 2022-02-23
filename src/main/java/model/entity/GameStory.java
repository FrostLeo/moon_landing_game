package model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import model.data.GameStoryDto;

import javax.persistence.*;

@Entity
@Table
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class GameStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(columnDefinition = "text")
    String gameStoryData;

    public GameStory() {
        gameStoryData = new GameStoryDto().getGameStoryListToJson();
    }
}
