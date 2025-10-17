package org.example.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.utils.UserServiceUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserBookingServices {
    private User user;

    private List<User> userList;

    // Store source and destination from search
    private String searchedSource;
    private String searchedDestination;

    private ObjectMapper object_Mapper = new ObjectMapper();

    private static final String USER_PATH = "D:\\Backend\\TicketBookingApp\\app\\src\\main\\java\\org\\example\\localdb\\users.json";

    public UserBookingServices(User user1) throws IOException {
        this.user = user1;
        loadusers();
    }

    public UserBookingServices() throws IOException {
        loadusers();
    }

    public List<User> loadusers() throws IOException {
        File users = new File(USER_PATH);
        if (!users.exists()) {
            // Create parent directories if they don't exist
            users.getParentFile().mkdirs();
            // Create the file with an empty array
            userList = new ArrayList<>();
            saveUserListToFile();
        } else {
            userList = object_Mapper.readValue(users,
                    new TypeReference<List<User>>() {
                    });
        }
        return userList;
    }

    public Boolean loginUser() {
        Optional<User> userFound = userList.stream()
                .filter(user1 -> user1.getName().equalsIgnoreCase(user.getName())
                        && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashPassword()))
                .findFirst();

        if (userFound.isPresent()) {
            // Store the found user data in this.user
            this.user = userFound.get();
            return true;
        }
        return false;
    }

    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }

    public void saveUserListToFile() throws IOException {
        object_Mapper.writeValue(new File(USER_PATH), userList);
    }

    public void fetchBooking() {
        if (user == null || user.getUserId() == null) {
            System.out.println("Please login first to view your bookings");
            return;
        }

        Optional<User> userFetched = userList.stream()
                .filter(user1 -> user1.getUserId() != null && user1.getUserId().equals(user.getUserId()))
                .findFirst();

        if (userFetched.isPresent()) {
            List<Ticket> tickets = userFetched.get().getTicketsBooked();
            if (tickets == null || tickets.isEmpty()) {
                System.out.println("No bookings found for user: " + user.getName());
            } else {
                System.out.println("Bookings for user: " + user.getName());
                userFetched.get().printTickets();
            }
        } else {
            System.out.println("User not found in the system.");
        }
    }

    public Boolean cancelBooking(String ticketId) {
        if (user == null || user.getUserId() == null) {
            System.out.println("Please login first to cancel tickets");
            return Boolean.FALSE;
        }

        // Find the user in userList
        Optional<User> currentUser = userList.stream()
                .filter(u -> u.getUserId() != null && u.getUserId().equals(user.getUserId()))
                .findFirst();

        if (currentUser.isPresent()) {
            User foundUser = currentUser.get();
            List<Ticket> userTickets = foundUser.getTicketsBooked();

            if (userTickets == null || userTickets.isEmpty()) {
                System.out.println("No tickets found for this user.");
                return Boolean.FALSE;
            }

            // Find and remove the ticket
            boolean removed = userTickets.removeIf(ticket -> ticket.getTicketId().equals(ticketId));

            if (removed) {
                // Update the user object with the modified tickets list
                foundUser.setTicketsBooked(userTickets);

                // Update the user in userList
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getUserId() != null && userList.get(i).getUserId().equals(user.getUserId())) {
                        userList.set(i, foundUser);
                        break;
                    }
                }

                // Update our reference to the user
                user = foundUser;

                try {
                    // Save changes to file
                    saveUserListToFile();
                    System.out.println("Ticket with ID " + ticketId + " has been canceled.");
                    return Boolean.TRUE;
                } catch (IOException e) {
                    System.out.println("Error saving changes: " + e.getMessage());
                    return Boolean.FALSE;
                }
            } else {
                System.out.println("No ticket found with ID " + ticketId + ".");
                return Boolean.FALSE;
            }
        } else {
            System.out.println("User not found in the system.");
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String destination) {
        try {
            System.out.println("Searching for trains from: " + source + " to: " + destination);

            // Store the source and destination for later use in booking
            this.searchedSource = source;
            this.searchedDestination = destination;

            TrainBookingServices trainService = new TrainBookingServices();
            List<Train> results = trainService.searchTrains(source, destination);
            System.out.println("Found " + results.size() + " matching trains");
            return results;
        } catch (Exception e) {
            System.out.println("Error searching for trains: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try {
            // Check if user is logged in
            if (user == null || user.getUserId() == null) {
                System.out.println("Please login first to book tickets");
                return Boolean.FALSE;
            }

            TrainBookingServices trainService = new TrainBookingServices();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    // Mark seat as booked
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);

                    // Create a new ticket - use the searched source and destination
                    String source, destination;

                    // Use searched values if available, otherwise default to first and last
                    // stations
                    if (searchedSource != null && searchedDestination != null) {
                        source = searchedSource;
                        destination = searchedDestination;
                    } else {
                        source = train.getStations().get(0);
                        destination = train.getStations().get(train.getStations().size() - 1);
                    }

                    String ticketId = UUID.randomUUID().toString();

                    // Create and add the ticket to user's bookings
                    Ticket newTicket = new Ticket(
                            ticketId,
                            user.getUserId(),
                            source,
                            destination,
                            java.time.LocalDate.now().toString(), // Today's date as default
                            train);

                    // Find the user in userList and update their tickets
                    Optional<User> currentUser = userList.stream()
                            .filter(u -> u.getUserId() != null && u.getUserId().equals(user.getUserId()))
                            .findFirst();

                    if (currentUser.isPresent()) {
                        User foundUser = currentUser.get();
                        List<Ticket> userTickets = foundUser.getTicketsBooked();
                        if (userTickets == null) {
                            userTickets = new ArrayList<>();
                        }
                        userTickets.add(newTicket);
                        foundUser.setTicketsBooked(userTickets);

                        // Update user in the class as well
                        user = foundUser;

                        // Save to file
                        saveUserListToFile();

                        System.out.println("Ticket booked with ID: " + ticketId);

                        // Clear the stored source and destination after booking
                        this.searchedSource = null;
                        this.searchedDestination = null;

                        return Boolean.TRUE;
                    } else {
                        System.out.println("Error: User not found in the database");
                        return Boolean.FALSE;
                    }
                } else {
                    return Boolean.FALSE; // Seat is already booked
                }
            } else {
                return Boolean.FALSE; // Invalid row or seat index
            }
        } catch (IOException ex) {
            System.out.println("Error booking seat: " + ex.getMessage());
            return Boolean.FALSE;
        }
    }

}
