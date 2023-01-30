package com.example.tripservicekata.trip;

import com.example.tripservicekata.exception.CollaboratorCallException;
import com.example.tripservicekata.exception.UserNotLoggedInException;
import com.example.tripservicekata.user.User;
import com.example.tripservicekata.user.UserSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
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

    // Fake
    public static class FakeUserSession extends UserSession {
        @Override
        public User getLoggedUser() {
            User user = new User();
            return user;
        }
    }

    // Fake
    public static class FakeUserSessionWithFriends extends UserSession {
        @Override
        public User getLoggedUser() {
            User user = new User();
            fakeUser.addFriend(user);
            user.addFriend(fakeUser);

            return fakeUser;
        }
    }

    public static User fakeUser = new User();

    @Test
    void UserSession이_Null인_경우() {

        UserSession userSession = new NullUserSession();
        Trip trip = new Trip();
        TripService tripService = new TripService(userSession);
        User user = new User();

        assertThrows(UserNotLoggedInException.class, () -> tripService.getTripsByUser(user));
    }

    @Test
    void UserSession이_예외를_일으키는_경우() {

        UserSession userSession = new FakeExceptionUserSession();
        Trip trip = new Trip();
        TripService tripService = new TripService(userSession);
        User user = new User();

        assertThrows(CollaboratorCallException.class, () -> tripService.getTripsByUser(user));
    }

    @Test
    void User의_Friends가_Null인_경우() {

        UserSession userSession = new FakeUserSession();
        Trip trip = new Trip();
        TripService tripService = new TripService(userSession);
        User user = new User();

        assertEquals(new ArrayList<>(), tripService.getTripsByUser(user));
    }

    @Test
    void User의_Friends가_Null이_아닌_경우() {
        UserSession userSession = new FakeUserSessionWithFriends();
        Trip trip = new Trip();
        TripService tripService = new TripService(userSession);

        User user = new User();

        user.addFriend(fakeUser);

        assertEquals(new ArrayList<>(), tripService.getTripsByUser(fakeUser));
    }

    @Test
    void User의_Friends가_Null이_아니며_같은_Friends인_경우() {
        UserSession userSession = new FakeUserSessionWithFriends();

        Trip trip1 = new Trip();
        Trip trip2 = new Trip();
        Trip trip3 = new Trip();

        List<Trip> tripList = new ArrayList<Trip>();
        tripList.add(trip1);
        tripList.add(trip2);
        tripList.add(trip3);

        TripService tripService = new TripServiceForTest(userSession, tripList);

        User user = new User();
        user.addFriend(fakeUser);

        assertEquals(tripList, tripService.getTripsByUser(user));
    }

    private static class TripServiceForTest extends TripService {

        private List<Trip> tripResult;

        public TripServiceForTest(UserSession userSession, List<Trip> tripResult) {
            super(userSession);
            this.tripResult = tripResult;
        }

        protected List<Trip> getTripsByUserFromDb(User user) {
            return tripResult;
        }
    }
}
