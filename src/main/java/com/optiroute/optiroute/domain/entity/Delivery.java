package com.optiroute.optiroute.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.optiroute.optiroute.domain.enums.DeliveryStatus;
import com.optiroute.optiroute.domain.vo.Coordinates;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "delivery")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Embedded
    @AttributeOverride(name="latitude", column=@Column(name="lat"))
    @AttributeOverride(name="longitude", column=@Column(name="lon"))
    private Coordinates coordinates;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "poids", nullable = false)
    private Double poids;

    @Column(name = "volume", nullable = false)
    private Double volume;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    @JsonIgnore
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private WareHouse warehouse;

    @Column(name = "is_optimized", nullable = false)
    private Boolean isOptimized = false;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;
}
