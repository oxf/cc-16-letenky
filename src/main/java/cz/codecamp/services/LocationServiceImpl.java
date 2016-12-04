package cz.codecamp.services;

import cz.codecamp.model.Location;
import cz.codecamp.model.User;
import cz.codecamp.repository.LocationRepository;
import cz.codecamp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by jakubbares on 12/1/16.
 */
public class LocationServiceImpl {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    UserRepository userRepository;

    public Location addLocation(String cityTo, String userName){
        User user = userRepository.findByUserName(userName);
        Location location = locationRepository.findByCity(cityTo);
        List<Location> locations = user.getCitiesTo();
        locations.add(location);
        user.setCitiesTo(locations);
        userRepository.save(user);
        return location;
    }

}
