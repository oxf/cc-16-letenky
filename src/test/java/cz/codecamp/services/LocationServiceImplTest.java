package cz.codecamp.services;

import cz.codecamp.model.Location;
import cz.codecamp.model.User;
import cz.codecamp.repository.LocationRepository;
import cz.codecamp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.*;

/**
 * Created by jakubbares on 12/4/16.
 */

@SpringBootTest
public class LocationServiceImplTest {

    @Autowired
    LocationService locationService;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    UserRepository userRepository;

    public void testAddLocation(){
//        locationService.save(new Location("Haag","Netherlands","haag_nl"));
        Location location = locationService.addLocation("Haag", "kubres");
        User user = userRepository.findByUserName("kubres");
//        assertThat(user.getCitiesTo().contains(location));

    }

}
