package com.optiroute.optiroute.seeder;

import com.github.javafaker.Faker;
import com.optiroute.optiroute.domain.entity.Delivery;
import com.optiroute.optiroute.domain.entity.Vehicule;
import com.optiroute.optiroute.domain.entity.WareHouse;
import com.optiroute.optiroute.domain.enums.DeliveryStatus;
import com.optiroute.optiroute.domain.enums.VehicleType;
import com.optiroute.optiroute.domain.repository.DeliveryRepository;
import com.optiroute.optiroute.domain.repository.TourRepository;
import com.optiroute.optiroute.domain.repository.VehicleRepository;
import com.optiroute.optiroute.domain.repository.WareHouseRepository;
import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataSeeder {

    private final WareHouseRepository wareHouseRepository;
    private final VehicleRepository vehicleRepository;
    private final TourRepository tourRepository;
    private final DeliveryRepository deliveryRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    public DataSeeder(WareHouseRepository wareHouseRepository,
                     VehicleRepository vehicleRepository,
                     TourRepository tourRepository,
                     DeliveryRepository deliveryRepository) {
        this.wareHouseRepository = wareHouseRepository;
        this.vehicleRepository = vehicleRepository;
        this.tourRepository = tourRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @PostConstruct
    @Transactional
    public void seed() {
        // Create warehouses
        WareHouse warehouse = WareHouse.builder()
                .openHour(LocalTime.of(8, 0))
                .closeHour(LocalTime.of(18, 0))
                .coordinates(new Coordinates(33.5, -7.0))
                .address(new Address("Casablanca", "Maroc", "XYZ", "75001"))
                .build();
        warehouse = wareHouseRepository.save(warehouse);

        // Create vehicles
        List<Vehicule> vehicles = createVehicles();

        // Create tours
        // List<Tour> tours = createTours(warehouse, vehicles);

        // Generate large list of deliveries
        List<Delivery> deliveries = generateMassiveDeliveryList(warehouse);

        // Shuffle deliveries to randomize their order
        Collections.shuffle(deliveries);

        // Save all deliveries
        deliveryRepository.saveAll(deliveries);
    }

    private List<Vehicule> createVehicles() {
        List<Vehicule> vehicles = new ArrayList<>();

        // Create different types of vehicles
        VehicleType[] vehicleTypes = {
                VehicleType.VAN,
                VehicleType.TRUCK,
                VehicleType.BIKE,
        };

        for (int i = 0; i < 10; i++) {
            Vehicule vehicle = Vehicule.builder()
                    .vehicleType(vehicleTypes[random.nextInt(vehicleTypes.length)])
                    .build();
            vehicles.add(vehicleRepository.save(vehicle));
        }

        return vehicles;
    }

    /*private List<Tour> createTours(WareHouse warehouse, List<Vehicule> vehicles) {
        List<Tour> tours = new ArrayList<>();

        for (Vehicule vehicle : vehicles) {
            Tour tour = Tour.builder()
                    .vehicule(vehicle)
                    .wareHouse(warehouse)
                    .deliveryList(new ArrayList<>())
                    .build();
            tours.add(tourRepository.save(tour));
        }

        return tours;
    }*/

    private List<Delivery> generateMassiveDeliveryList(WareHouse warehouse) {
        return IntStream.range(0, 30)
                .mapToObj(i -> {

                    // Generate random coordinates around the world
                    double latitude = warehouse.getCoordinates().latitude() + faker.number().randomDouble(4, -50, 50) / 1000.0;
                    double longitude = warehouse.getCoordinates().longitude() + faker.number().randomDouble(4, -50, 50) / 1000.0;


                    return Delivery.builder()
                            .uuid(UUID.randomUUID())
                            .coordinates(new Coordinates(latitude, longitude))
                            .tour(null)
                            .deliveryStatus(DeliveryStatus.PENDING)
                            .poids(faker.number().randomDouble(2, 1, 50))  // Weight between 1 and 50 kg
                            .volume(faker.number().randomDouble(2, 1, 3))  // Volume between 1 and 25 cubic meters
                            .warehouse(warehouse)
                            .isOptimized(false)
                            .build();
                })
                .collect(Collectors.toList());
    }
}