package com.optiroute.optiroute.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.optiroute.optiroute.domain.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "vehicule")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;


    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Tour> tours;
}
