package config;

import model.entity.GameStory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HibernateConfig {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    // инициализация соединения с БД в отдельном методе для запуска при поднятии сервера
    public static void initSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder registryBuilder =
                        new StandardServiceRegistryBuilder();

                Map<String, String> settings = new HashMap<>();
                Properties dbProp = getProperties();
                settings.put(Environment.DRIVER, dbProp.getProperty("DRIVER"));
                settings.put(Environment.URL, dbProp.getProperty("URL"));
                settings.put(Environment.USER, dbProp.getProperty("USER"));
                settings.put(Environment.PASS, dbProp.getProperty("PASS"));
                settings.put(Environment.DIALECT, dbProp.getProperty("DIALECT"));
                settings.put(Environment.HBM2DDL_AUTO, dbProp.getProperty("DDL"));

                registryBuilder.applySettings(settings);
                registry = registryBuilder.build();

                MetadataSources sources = new MetadataSources(registry);
                sources.addAnnotatedClass(GameStory.class);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                close();
            }
        }
    }

    // Singleton
    public static SessionFactory getSessionFactory() {
        initSessionFactory();
        return sessionFactory;
    }

    private static Properties getProperties() {
        Properties result = new Properties();
        try (FileReader reader = new FileReader("./src/main/resources/db.properties")) {
            result.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void close() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
