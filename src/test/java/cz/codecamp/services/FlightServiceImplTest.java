package cz.codecamp.services;

import cz.codecamp.model.Flight;
import cz.codecamp.model.Location;
import cz.codecamp.model.User;
import cz.codecamp.repository.FlightRepository;
import cz.codecamp.repository.UserRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Created by jakubbares on 12/4/16.
 */

@SpringBootTest
public class FlightServiceImplTest {

    @Autowired
    FlightService flightService;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    UserRepository userRepository;

    @Before
    public void cleanDatabase() {
        flightRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Before
    public void prepareUser() throws ParseException {
        User user = new User();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateFromString = "05.12.2016";
        String dateToString = "01.01.2017";
        Date dateFrom = dateFormat.parse(dateFromString);
        Date dateTo = dateFormat.parse(dateToString);

        user.setCityFrom(new Location("Prague", "Czech republic", "prague_cz"));
        List<Location> citiesTo = new ArrayList<Location>();
        citiesTo.add(new Location("Berlin", "Germany", "berlin_de"));
        user.setCitiesTo(citiesTo);
        user.setDateTo(dateTo);
        user.setDateFrom(new Date());
        user.setEmailLogin("kubres@gmail.com");
        user.setPassword("123");
        user.setUserName("kubres");
        user.setPctAvgPriceMax(0.7);
        user.setFlyDurationHoursToMinutesMax(6);

        try{
            userRepository.save(user);
        } catch(Exception e) {
            System.err.println("Caught Exception: " + e.getMessage());
        }
    }

    public void testSaveFlightsFromJson() throws IOException {
        List<Flight> flights = flightService.saveFlightsFromJson();
        assertEquals(flights.get(0).getCityTo(), "berlin_cz");
        assertEquals(flights.get(0).getDepDate(), new Date(1481000000000L));
    }

    public void testGetFlightsForUser(){
        List<Flight> flights = flightService.getFlightsForUser("kubres@gmail.com");
        assertEquals(flights.get(0).getCityTo(), "berlin_cz");
        assertEquals(flights.get(0).getPrice(), 5500);
    }


}
