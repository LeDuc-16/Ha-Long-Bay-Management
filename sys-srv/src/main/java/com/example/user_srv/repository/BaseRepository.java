package com.example.user_srv.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public abstract class BaseRepository<T> {

    @PersistenceContext
    protected EntityManager entityManager;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int QUERY_BATCH_SIZE;

    public void saveOrUpdate(T entity) {
        if (getId(entity) == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    @Transactional
    public List<T> saveAll(List<T> entities) {
        for (int i = 0; i < entities.size(); i++) {
            saveOrUpdate(entities.get(i));

            if (i > 0 && i % QUERY_BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();

        return entities;
    }

    protected abstract Object getId(T entity);

}
