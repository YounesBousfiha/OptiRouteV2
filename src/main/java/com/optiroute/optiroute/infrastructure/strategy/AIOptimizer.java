package com.optiroute.optiroute.infrastructure.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.optiroute.optiroute.domain.entity.*;
import com.optiroute.optiroute.domain.repository.DeliveryHistoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "optimizer.strategy", havingValue = "ai", matchIfMissing = true)
@Slf4j
public class AIOptimizer implements TourOptimizer {

    private final int SAMPLES_SIZE = 15;

    @Autowired
    private DeliveryHistoryRepository deliveryHistoryRepository;

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    private ChatClient chatClient;


    @PostConstruct
    public void init() {
        log.warn("AI Optimizer Initializer");
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public List<Delivery> calculateOptimalTour(WareHouse wareHouse, List<Delivery> deliveryList, Vehicule vehicule) {
        log.warn("AI Optimizer Start Claculation");
        List<DeliveryHistory> deliveryHistories = getRandomSamples();
        log.warn("deliveries History Samlpes : {}", deliveryHistories);
       ObjectNode prompt = buildPrompt(wareHouse, deliveryList, vehicule, deliveryHistories);
       log.warn("Prompt Completed: {}", prompt.toPrettyString());
       String aiResponse = callAI(prompt);
       log.warn("AI response : {} ", aiResponse);
       return parseTour(aiResponse, deliveryList);
    }


    private List<DeliveryHistory>  getRandomSamples() {

        return this.deliveryHistoryRepository.findRandomSamples(SAMPLES_SIZE);
    }

    private ObjectNode buildPrompt(WareHouse wareHouse, List<Delivery> deliveryList, Vehicule vehicule, List<DeliveryHistory> deliveryHistories) {
        log.warn("Start Building a Prompt ");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.putObject("system").put("role", "system")
                .put("content", "You are TourOptimizer, an AI that optimizes delivery tours considering vehicle capacity and past patterns. You MUST respond with ONLY valid JSON. Do NOT use markdown code fences, do NOT add explanatory text before or after the JSON.");

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
        task.put("output_instructions", "Return ONLY raw JSON without markdown formatting. No code blocks, no backticks, no text explanations.");

        ObjectNode outputFormat = task.putObject("output_format");
        outputFormat.put("type", "json");
        outputFormat.put("strict", true);
        ArrayNode fields = outputFormat.putArray("required_fields");
        fields.add("optimized_deliveries");
        fields.add("recommendations");
        fields.add("predicted_best_routes");

        ObjectNode schema = task.putObject("required_schema");
        schema.put("predicted_best_routes_structure", "Array of route objects. Each route MUST contain a 'sequence' field (array of delivery IDs in optimal order)");
        schema.put("sequence_field_name", "Use exactly 'sequence' as the field name, not 'route_sequence' or other variations");

        return root;
    }

    private String callAI(ObjectNode promptText) {
        return this.chatClient.prompt(promptText.toString())
                .call()
                .content();
    }

    private List<Delivery> parseTour(String aiResponse, List<Delivery> originalDeliveries) {
        ObjectMapper mapper = new ObjectMapper();
        List<Delivery> orderedDeliveries = new ArrayList<>();

        try {
            String cleanedResponse = aiResponse.trim();

            if(cleanedResponse.startsWith("```")) {

                cleanedResponse = cleanedResponse.replaceFirst("```(?:json)?\\s*", "");
                cleanedResponse = cleanedResponse.replaceFirst("```\\s*$", "");
                cleanedResponse = cleanedResponse.trim();
            }

            JsonNode root = mapper.readTree(cleanedResponse);
            JsonNode routes = root.get("predicted_best_routes");

            if(null == routes || !routes.isArray() || routes.isEmpty()) {
                throw new RuntimeException("No predicted_best_routes found in AI response");
            }

            JsonNode firstRoute = routes.get(0);
            JsonNode sequence = firstRoute.get("sequence");

            if(null == sequence || !sequence.isArray()) {
                throw new RuntimeException("No sequence found in routes");
            }

            for(JsonNode idNode : sequence) {
                Long deliveryId = idNode.asLong();
                originalDeliveries.stream()
                        .filter(del -> del.getId().equals(deliveryId))
                        .findFirst()
                        .ifPresent(orderedDeliveries::add);
            }

            log.info("Parsed {} deliveries from AI response sequence", orderedDeliveries.size());
        } catch (Exception e) {
            log.error("Failed to parse AI response: {}", e.getMessage());
            throw  new RuntimeException("Failed to parse AI response", e);
        }

        return orderedDeliveries;
    }
}
