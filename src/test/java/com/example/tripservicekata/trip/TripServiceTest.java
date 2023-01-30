package com.example.tripservicekata.trip;

import com.example.tripservicekata.exception.CollaboratorCallException;
import com.example.tripservicekata.exception.UserNotLoggedInException;
import com.example.tripservicekata.user.User;
import com.example.tripservicekata.user.UserSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class TripServiceTest {

    // Fake
    public static class NullUserSession extends UserSession {
        @Override
        public User getLoggedUser() {
            return null;
        }
    }

    // Fake
    public static class FakeExceptionUserSession extends UserSession {
        @Override
        public User getLoggedUser() {
            return super.getLoggedUser();
        }
    }

    @Test
    void UserSession이_Null인_경우() {

        UserSession userSession = new NullUserSession();
        Trip trip = new Trip();
        TripService tripService = new TripService(userSession);
        User user = new User();

        assertThrows(UserNotLoggedInException.class, () -> tripService.getTripsByUser(user));
    }

    @Test
    void UserSession이_Null이_아닌_경우() {

        UserSession userSession = new FakeExceptionUserSession();
        Trip trip = new Trip();
        TripService tripService = new TripService(userSession);
        User user = new User();

        assertThrows(CollaboratorCallException.class, () -> tripService.getTripsByUser(user));
    }

}
