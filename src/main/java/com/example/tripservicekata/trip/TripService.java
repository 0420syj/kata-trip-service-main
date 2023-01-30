package com.example.tripservicekata.trip;

import com.example.tripservicekata.exception.UserNotLoggedInException;
import com.example.tripservicekata.user.User;
import com.example.tripservicekata.user.UserSession;

import java.util.ArrayList;
import java.util.List;

public class TripService {

	private UserSession userSession;
	private Trips trips;

	public TripService(UserSession userSession) {
		this.userSession = userSession;
	}
	
	public TripService(UserSession userSession, Trips trips) {
		this.userSession = userSession;
		this.trips = trips;
	}

	public List<Trip> getTripsByUser(User user) throws UserNotLoggedInException {
		List<Trip> tripList = new ArrayList<Trip>();
		User loggedUser = this.userSession.getLoggedUser();
		boolean isFriend = false;
		if (loggedUser != null) {
			for (User friend : user.getFriends()) {
				if (friend.equals(loggedUser)) {
					isFriend = true;
					break;
				}
			}
			if (isFriend) {
				tripList = getTripsByUserFromDb(user);
			}
			return tripList;
		} else {
			throw new UserNotLoggedInException();
		}
	}

	protected List<Trip> getTripsByUserFromDb(User user) {
		return TripDAO.findTripsByUser(user);
	}
}
