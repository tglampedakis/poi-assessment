package com.example.poiassessment.dto.entity;

import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;


@Entity
@Table(name = "poi")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Poi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "counter")
    private Integer counter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Poi poi = (Poi) o;
        return getId() != null && Objects.equals(getId(), poi.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
