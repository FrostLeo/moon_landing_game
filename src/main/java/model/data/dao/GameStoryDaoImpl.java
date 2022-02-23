package model.data.dao;

import config.HibernateConfig;
import model.entity.GameStory;
import org.hibernate.Session;

//import java.util.List;
import java.util.Optional;

public class GameStoryDaoImpl implements GameStoryDao {
    public void save(GameStory gameStory) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(gameStory);
            session.flush();
        }
    }

    @Override
    public void update(GameStory gameStory) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(gameStory);
            session.flush();
        }
    }

    @Override
    public Optional<GameStory> getById(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            // Spacecraft result = session.get(SpaceLanderDto.class, id).toEntity();
            return Optional.ofNullable(session.get(GameStory.class, id));
        }
    }

//    @Override
//    public void delete(Long id) {
//        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
//            session.beginTransaction();
//            if (getById(id).isPresent()) {
//                session.delete(getById(id).get());
//            }
//            session.flush();
//        }

//    }
//    @Override
//    public List<GameStory> getAll() {
//        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
//            List<GameStory> result = session.createCriteria(GameStory.class).list();
//            return result;
//        }

//    }
}
