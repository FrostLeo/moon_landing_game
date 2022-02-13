package model.story.dao;

import config.HibernateConfig;
import model.dto.GameStoryDto;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class GameStoryDaoImpl implements GameStoryDao {
    public void save(GameStoryDto gameStoryDto) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(gameStoryDto);
            session.flush();
        }
    }

    @Override
    public void update(GameStoryDto gameStoryDto) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(gameStoryDto);
            session.flush();
        }
    }

//    public void delete(Long id) {
//        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
//            session.beginTransaction();
//            if (getById(id).isPresent()) {
//                session.delete(getById(id).get());
//            }
//            session.flush();
//        }
//    }

    public List<GameStoryDto> getAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            List<GameStoryDto> result = session.createCriteria(GameStoryDto.class).list();
//        List<SpaceLanderDto> dtoList = session.createCriteria(SpaceLanderDto.class).list();
//        List<Spacecraft> result = dtoList.stream()
//                .map(dto -> dto.toEntity())
//                .collect(Collectors.toList());
            return result;
        }
    }

    public Optional<GameStoryDto> getById(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            // Spacecraft result = session.get(SpaceLanderDto.class, id).toEntity();
            return Optional.ofNullable(session.get(GameStoryDto.class, id));
        }
    }
}
