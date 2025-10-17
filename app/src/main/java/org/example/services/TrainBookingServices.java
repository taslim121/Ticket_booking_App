package org.example.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.example.entities.Train;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TrainBookingServices {

    private List<Train> trainList;
    private ObjectMapper object_Mapper = new ObjectMapper();
    private static final String TRAIN_PATH = "D:\\Backend\\TicketBookingApp\\app\\src\\main\\java\\org\\example\\localdb\\trains.json";

    public TrainBookingServices() throws IOException {
        File trains = new File(TRAIN_PATH);
        if (!trains.exists()) {
            // Create parent directories if they don't exist
            trains.getParentFile().mkdirs();
            // Create the file with an empty array
            trainList = new ArrayList<>();
            saveTrainListToFile();
        } else {
            trainList = object_Mapper.readValue(trains, new TypeReference<List<Train>>() {
            });
            System.out.println("Loaded " + trainList.size() + " trains from database");
        }
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream()
                .filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());
    }

    private Boolean validTrain(Train train, String source, String destination) {
        List<String> stations = train.getStations();

        // Find stations using case-insensitive comparison
        Optional<String> sourceStation = stations.stream()
                .filter(s -> s.equalsIgnoreCase(source))
                .findFirst();

        Optional<String> destStation = stations.stream()
                .filter(s -> s.equalsIgnoreCase(destination))
                .findFirst();

        // If both stations found, check if source comes before destination
        if (sourceStation.isPresent() && destStation.isPresent()) {
            int sourceIndex = stations.indexOf(sourceStation.get());
            int destIndex = stations.indexOf(destStation.get());
            return sourceIndex < destIndex;
        }

        return false;
    }

    public void addTrain(Train newTrain) {
        // Check if a train with the same trainId already exists
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            // If a train with the same trainId exists, update it instead of adding a new
            // one
            updateTrain(newTrain);
        } else {
            // Otherwise, add the new train to the list
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) {
        // Find the index of the train with the same trainId
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            // If found, replace the existing train with the updated one
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            // If not found, treat it as adding a new train
            addTrain(updatedTrain);
        }
    }

    private void saveTrainListToFile() {
        try {
            object_Mapper.writeValue(new File(TRAIN_PATH), trainList);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception based on your application's requirements
        }
    }

}
