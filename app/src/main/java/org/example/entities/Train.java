package org.example.entities;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Train {

    @JsonProperty("trainId")
    @JsonAlias({ "train_id" })
    private String trainId;

    @JsonProperty("trainNo")
    @JsonAlias({ "train_no" })
    private String trainNo;

    private List<List<Integer>> seats;

    @JsonProperty("stationTime")
    @JsonAlias({ "station_time" })
    private Map<String, String> stationTime;

    private List<String> stations;

    public Train(String trainId, String trainNo, List<List<Integer>> seats, Map<String, String> stationTime,
            List<String> stations) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTime = stationTime;
        this.stations = stations;
    }

    public Train() {

    }

    public String getTrainId() {
        return trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public Map<String, String> getStationTime() {
        return stationTime;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public void setStationTime(Map<String, String> stationTime) {
        this.stationTime = stationTime;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    @JsonIgnore
    public String getTrainInfo() {
        return String.format("Train No: %s, Train ID: %s", trainNo, trainId);
    }

}
