package com.example.tripservicekata.trip;

import com.example.tripservicekata.user.User;

import java.util.ArrayList;
import java.util.List;

public class Trips {

    private List<Trip> tripList = new ArrayList<>();
    public Trips(List<Trip> tripList) {
        this.tripList = tripList;
    }

    private void setTripListByUser(User user) {
        this.tripList = TripDAO.findTripsByUser(user);
    }

    public List<Trip> getTripListByUser(User user) {
        setTripListByUser(user);
        return tripList;
    }
}
