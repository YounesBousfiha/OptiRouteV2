package com.optiroute.optiroute.application.service;

import com.optiroute.optiroute.application.mapper.VehiculeMapper;
import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.domain.repository.VehicleRepository;
import com.optiroute.optiroute.presentation.dto.request.VehiculeRequestDTO;
import com.optiroute.optiroute.presentation.dto.response.VehiculeResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehiculeService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehiculeService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public VehiculeResponseDTO createVehicule(VehiculeRequestDTO dto) {
        Vehicule vehicule = VehiculeMapper.toEntity(dto);

        Vehicule newVehicule = this.vehicleRepository.save(vehicule);

        return VehiculeMapper.toResponseDTO(newVehicule);
    }


    public List<VehiculeResponseDTO>  getAllVehicules() {

        List<Vehicule> vehiculeList = this.vehicleRepository.findAll();

        return vehiculeList.stream()
                .map(VehiculeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VehiculeResponseDTO getVehiculeById(Long id) {
        Optional<Vehicule> vehicule = this.vehicleRepository.findById(id);
        return vehicule.map(VehiculeMapper::toResponseDTO).orElse(null);
    }


    public void updateVehicule(Long id, VehiculeRequestDTO dto) {
        Optional<Vehicule> vehicule = this.vehicleRepository.findById(id);

        if(vehicule.isPresent()) {
            vehicule.get()
                    .setVehicleType(dto.getVehicleType());

            this.vehicleRepository.save(vehicule.get());
        }
    }

    public void deleteVehicule(Long id) {
        Optional<Vehicule> vehicule = this.vehicleRepository.findById(id);

        if(vehicule.isPresent()) {
            this.vehicleRepository.deleteById(id);
        }
    }



}
