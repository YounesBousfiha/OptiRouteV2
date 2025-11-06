package com.optiroute.optiroute.presentation.dto.response;

import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import com.optiroute.optiroute.domain.vo.PreferredTimeSlot;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponseDTO {

    private Long id;
    private String name;
    private Address address;
    private Coordinates coordinates;
    private PreferredTimeSlot preferredTimeSlot;
    private List<DeliveryResponseDTO> deliveryResponseDTOS;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
