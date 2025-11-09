package com.optiroute.optiroute.infrastructure.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.optiroute.optiroute.domain.entity.*;
import com.optiroute.optiroute.domain.repository.DeliveryHistoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "optimizer.strategy", havingValue = "ai", matchIfMissing = true)
public class AIOptimizer implements TourOptimizer {

    private final int SAMPLES_SIZE = 15;
    private final DeliveryHistoryRepository deliveryHistoryRepository;
    private final ChatClient chatClient;

    public AIOptimizer(ChatClient.Builder chatClientBuilder, DeliveryHistoryRepository deliveryHistoryRepository) {
        this.chatClient = chatClientBuilder.build();
        this.deliveryHistoryRepository = deliveryHistoryRepository;
    }

    @Override
    public List<Delivery> calculateOptimalTour(WareHouse wareHouse, List<Delivery> deliveryList, Vehicule vehicule) {

        List<DeliveryHistory> deliveryHistories = getRandomSamples();
        ObjectNode prompt = buildPrompt(wareHouse, deliveryList, vehicule, deliveryHistories);
        String aiResponse = callAI(prompt);

        return parseTour(aiResponse, vehicule);
    }


    private List<DeliveryHistory>  getRandomSamples() {
        return this.deliveryHistoryRepository.findRandomSamples(SAMPLES_SIZE);
    }

    private ObjectNode buildPrompt(WareHouse wareHouse, List<Delivery> deliveryList, Vehicule vehicule, List<DeliveryHistory> deliveryHistories) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.putObject("system").put("role", "system")
                .put("content", "You are TourOptimizer, an AI that optimizes delivery tours considering vehicle capacity and past patterns.");

        root.put("WareHouse", "Latitude: " + wareHouse.getCoordinates().latitude() + " " + "Longtitude: " + wareHouse.getCoordinates().longitude());
        ObjectNode vehiculeNode = root.putObject("vehicule");
        vehiculeNode.put("vehicle_id", vehicule.getId());
        vehiculeNode.put("capacity_kg", vehicule.getVehicleType().getMaxWeightKg());
        vehiculeNode.put("max_volume_m3", vehicule.getVehicleType().getMaxVolumeM3());

        ArrayNode historyArr = root.putArray("delivery_history_sample");
        for(DeliveryHistory d : deliveryHistories) {
            ObjectNode node = historyArr.addObject();
            node.put("delivery_id", d.getId());
            node.put("Longitude", d.getDelivery().getCoordinates().longitude());
            node.put("Latitude", d.getDelivery().getCoordinates().latitude());
            node.put("weight_kg", d.getDelivery().getPoids());
            node.put("volume_m3", d.getDelivery().getVolume());
        }

        ArrayNode newArr = root.putArray("new_deliveries");
        for(Delivery d : deliveryList) {
            ObjectNode node = newArr.addObject();
            node.put("delivery_id", d.getId());
            node.put("Longitude", d.getCoordinates().longitude());
            node.put("Latitude", d.getCoordinates().latitude());
            node.put("weight_kg", d.getPoids());
            node.put("volume_m3", d.getVolume());
        }

        ObjectNode task = root.putObject("task");
        task.put("objective", "Optimize delivery sequence for efficiency, capacity constraints and recommendations with justifications");
        task.putObject("output_format").putArray("fields")
                .add("optimized_deliveries")
                .add("recommendations")
                .add("predicted_best_routes");

        return root;
    }

    private String callAI(ObjectNode prompt) {
        return this.chatClient.prompt(prompt.toString())
                .call()
                .content();
    }

    private List<Delivery> parseTour(String aiResponse, Vehicule vehicule, List<Delivery> originalDeliveries) {
        ObjectMapper mapper = new ObjectMapper();
        List<Delivery> orderedDelivries = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(aiResponse);
            JsonNode optimizedArr = root.get("optimized_deliveries");

            for (JsonNode d : optimizedArr) {
                Long deliveryId = d.get("delivery_id").asLong();
                originalDeliveries.stream()
                        .filter(del -> del.getId().equals(deliveryId))
                        .findFirst()
                        .ifPresent(originalDeliveries::add);
            }
        } catch (Exception e) {
            throw  new RuntimeException("Failed to parse AI response", e);
        }

        return orderedDelivries;
    }
}
