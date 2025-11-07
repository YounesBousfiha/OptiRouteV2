package com.optiroute.optiroute.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

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

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "planned_delivery_time", nullable = false)
    private LocalTime plannedDeliveryTime;

    @Column(name = "actual_delivery_time", nullable = false)
    private LocalTime actualDeliveryTime;

    @Column(name = "delay_in_minutes", nullable = false)
    private Long delayInMinutes;

    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;
}

