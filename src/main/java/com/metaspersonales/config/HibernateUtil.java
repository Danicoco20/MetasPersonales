package com.metaspersonales.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/** Centraliza la creación y el cierre del EntityManagerFactory de Hibernate. */
public final class HibernateUtil {

    private static final String PERSISTENCE_UNIT = "metasPersonalesPU";
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = buildFactory();

    private HibernateUtil() { }

    private static EntityManagerFactory buildFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        properties.put("jakarta.persistence.jdbc.url", env("DB_URL",
            "jdbc:mysql://localhost:3306/BD_Metas_personales?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true"));
        properties.put("jakarta.persistence.jdbc.user", env("DB_USER", "root"));
        properties.put("jakarta.persistence.jdbc.password", env("DB_PASSWORD", ""));
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "true");
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            value = System.getProperty(name);
        }
        return value == null ? defaultValue : value;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (!ENTITY_MANAGER_FACTORY.isOpen()) {
            throw new IllegalStateException("La fábrica de EntityManager está cerrada.");
        }
        return ENTITY_MANAGER_FACTORY;
    }

    public static void shutdown() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}
