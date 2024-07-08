//package com.example.poiassessment.repository;
//
//import com.example.poiassessment.dto.PointRequestDto;
//import com.example.poiassessment.dto.entity.Poi;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;
//import javax.persistence.TypedQuery;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class PoiRepositoryTest {
//
//    @Mock
//    private EntityManager em;
//
//    @Mock
//    private TypedQuery<Poi> query;
//
//    @Mock
//    private EntityTransaction transaction;
//
//    @InjectMocks
//    PoiRepository poiRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSavePoint() {
//        PointRequestDto pointRequestDto = new PointRequestDto();
//        pointRequestDto.setName("Test Point");
//        pointRequestDto.setLatitude(45.0);
//        pointRequestDto.setLongitude(90.0);
//
//        EntityTransaction transaction = mock(EntityTransaction.class);
//        when(em.getTransaction()).thenReturn(transaction);
//
//        poiRepository.savePoint(pointRequestDto);
//
//        verify(transaction).begin();
//        verify(transaction).commit();
//        verify(em).persist(any(Poi.class));
//    }
//
//    @Test
//    void testRetrieveHigherCounterPoints() {
//        Integer threshold = 5;
//
//        Poi poi1 = new Poi();
//        poi1.setId(1L);
//        poi1.setName("Point1");
//        poi1.setCounter(10);
//
//        Poi poi2 = new Poi();
//        poi2.setId(2L);
//        poi2.setName("Point2");
//        poi2.setCounter(20);
//
//        List<Poi> expectedPoints = Arrays.asList(poi1, poi2);
//
//        when(em.createQuery(anyString(), eq(Poi.class))).thenReturn(query);
//        when(query.setParameter("threshold", threshold)).thenReturn(query);
//        when(query.getResultList()).thenReturn(expectedPoints);
//
//        List<Poi> result = poiRepository.retrieveHigherCounterPoints(threshold);
//
//        assertEquals(expectedPoints, result);
//
//        verify(em).createQuery("SELECT p FROM Poi p WHERE p.counter > :threshold", Poi.class);
//        verify(query).setParameter("threshold", threshold);
//        verify(query).getResultList();
//    }
//
//}
