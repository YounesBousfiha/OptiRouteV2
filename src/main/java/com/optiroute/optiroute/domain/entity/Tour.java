package com.optiroute.optiroute.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tour")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private WareHouse wareHouse;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Delivery> deliveryList;

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "tour_date")
    private LocalDate tourDate;

    @Column(name = "totalDistance")
    private double totalDistance;

    @Transient
    private Long vehiculeId;

    @Transient
    private Long wareHouseId;
}
