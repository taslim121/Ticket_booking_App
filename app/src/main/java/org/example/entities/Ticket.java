package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {

    private String ticketId;

    private String userId;

    private String source;

    private String destination;

    private String dateOfTravel;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Train train;

    // Default constructor for JSON deserialization
    public Ticket() {
        // Empty constructor needed for Jackson
    }

    public Ticket(String ticketId, String userId, String source, String destination, String dateOfTravel,
            Train train) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
    }

    @JsonIgnore
    public String getTicketInfo() {
        return String.format("Ticket ID: %s, User ID: %s, From: %s, To: %s, Date of Travel: %s, Train No: %s", ticketId,
                userId, source, destination, dateOfTravel, train.getTrainNo());
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDateOfTravel() {
        return dateOfTravel;
    }

    public Train getTrain() {
        return train;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

}
