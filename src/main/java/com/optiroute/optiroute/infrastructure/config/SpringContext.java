package com.optiroute.optiroute.infrastructure.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class SpringContext {

    private final EntityManagerFactory emf;

    public SpringContext(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void closeEmf() {
        if (emf.isOpen()) {
            emf.close();
        }
    }

    public void closeEm(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

}
