package com.optiroute.optiroute.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "warehouse")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WareHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Embedded
    @AttributeOverride(name="latitude", column=@Column(name="lat"))
    @AttributeOverride(name="longitude", column=@Column(name="lon"))
    private Coordinates coordinates;

    @Column(name = "openHour", nullable = false)
    private LocalTime openHour;

    @Column(name = "closeHour", nullable = false)
    private LocalTime closeHour;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "wareHouse")
    @JsonIgnore
    private List<Tour> tours;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnore
    private List<Delivery> availableDeliveries;

    @Column(name = "createdAt", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Override
    public String toString() {
        return "WareHouse{" +
                "id=" + id +
                ", coordinates=" + coordinates +
                ", openHour=" + openHour +
                ", closeHour=" + closeHour +
                ", address=" + address +
                ", createdAt=" + createdAt +
                '}';
    }

    // Optionally, if you want to break potential circular references
    @Override
    public int hashCode() {
        return Objects.hash(id, coordinates, openHour, closeHour);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WareHouse wareHouse = (WareHouse) o;
        return Objects.equals(id, wareHouse.id) &&
                Objects.equals(coordinates, wareHouse.coordinates);
    }
}
