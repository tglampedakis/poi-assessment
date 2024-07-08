package com.example.poiassessment.repository;

import com.example.poiassessment.dto.PointRequestDto;
import com.example.poiassessment.dto.entity.Poi;
import jakarta.enterprise.context.ApplicationScoped;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PoiRepository {

    private static final String PERSISTENCE_UNIT_NAME = "POI_PU";
    private final EntityManager em;

    public PoiRepository() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = emf.createEntityManager();
    }

    @Transactional
    public void savePoint(PointRequestDto pointRequestDto) {
        em.getTransaction().begin();

        Poi point = new Poi();
        point.setName(pointRequestDto.getName());
        point.setLatitude(pointRequestDto.getLatitude());
        point.setLongitude(pointRequestDto.getLongitude());
        point.setCounter(0);

        em.persist(point);
        em.getTransaction().commit();
    }

    public List<Poi> retrieveClosestPoint(double lat, double lon) {
        return em.createQuery(
                        "SELECT p " +
                        "FROM Poi p " +
                        "ORDER BY " +
                        "    (6371 * acos( " +
                        "            cos(radians(:lat)) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians(:lon)) " +
                        "            + sin(radians(:lat)) * sin(radians(p.latitude)) " +
                        "        ) " +
                        "    ) ASC", Poi.class)
                .setParameter("lat", lat)
                .setParameter("lon", lon)
                .setMaxResults(1)
                .getResultList();
    }

    @Transactional
    public void incrementPointCounter(Poi point) {
        em.detach(point);
        point.setCounter(point.getCounter() + 1);
        em.getTransaction().begin();
        em.merge(point);
        em.getTransaction().commit();
    }

    public List<Poi> retrieveHigherCounterPoints(Integer threshold) {
        return em.createQuery("SELECT p FROM Poi p WHERE p.counter > :threshold", Poi.class)
                .setParameter("threshold", threshold)
                .getResultList();
    }

    public List<Poi> retrievePoint(Long pointId) {
        return em.createQuery("SELECT p FROM Poi p WHERE p.id = :pointId", Poi.class)
                .setParameter("pointId", pointId)
                .getResultList();
    }

    public List<Poi> retrieveAll() {
        return em.createQuery("select p from Poi p", Poi.class).getResultList();
    }

}
