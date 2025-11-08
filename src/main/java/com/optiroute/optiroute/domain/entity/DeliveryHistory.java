package com.optiroute.optiroute.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "delivery_history")
public class DeliveryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(name = "planned_delivery_time", nullable = false)
    private LocalDateTime plannedDeliveryTime;

    @Column(name = "actual_delivery_time", nullable = false)
    private LocalDateTime actualDeliveryTime;

    @Column(name = "delay_in_minutes", nullable = false)
    private Long delayInMinutes;

    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;
}

