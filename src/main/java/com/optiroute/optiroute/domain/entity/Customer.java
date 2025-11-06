package com.optiroute.optiroute.domain.entity;


import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import com.optiroute.optiroute.domain.vo.PreferredTimeSlot;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Embedded
    private Address address;

    @Embedded
    @AttributeOverride(name="latitude", column=@Column(name="lat"))
    @AttributeOverride(name="longitude", column=@Column(name="lon"))
    private Coordinates coordinates;

    @Embedded
    private PreferredTimeSlot preferredTimeSlot;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveryList;
}
