package cz.codecamp.controllers;

import cz.codecamp.commands.LoginCommand;
import cz.codecamp.model.Flight;
import cz.codecamp.model.Location;
import cz.codecamp.model.User;
import cz.codecamp.repository.FlightRepository;
import cz.codecamp.repository.LocationRepository;
import cz.codecamp.repository.UserRepository;
import cz.codecamp.services.FlightService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by jt on 2/2/16.
 */
@Controller
public class LoginController {
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FlightRepository flightRepository;
    @Autowired
    FlightService flightService;

    @RequestMapping({"/", "signin"})
    public String showLoginForm(Model model){
        return "cities";
    }

    @RequestMapping("/logout")
    public String yourLoggedOut(){
        return "logout";
    }

    @RequestMapping("registration")
    public String register(){
        return "registration";
    }

    @RequestMapping(value = "flights", method = RequestMethod.GET)
    public ModelAndView testFlights(){
        ModelAndView modelAndView = new ModelAndView("flights");
        User user = userRepository.findByUserName("kubres");
        String emailLogin = user.getEmailLogin();
        modelAndView.addObject("user", user);
        List<Flight> flights = flightService.getFlightsForUser(emailLogin);
        modelAndView.addObject("flights",flights );
        return modelAndView;
    }

    @GetMapping("settings")
    public ModelAndView getSettings(){
        ModelAndView modelAndView = new ModelAndView("settings");
        User user = userRepository.findByUserName("kubres");
        modelAndView.addObject("user",user);
        return modelAndView;
    }

    @PostMapping("settings")
    public String postSettings(@ModelAttribute User user){
//        userRepository.save(user);
        return "settings-result";
    }

    @GetMapping("/cities")
    public String addCity(Model model){
        model.addAttribute("location", new Location());
        User user = userRepository.findByUserName("kubres");
        model.addAttribute("citiesTo", locationRepository.findAll());
        model.addAttribute("user", user);
        return "cities";
    }

    @PostMapping("/cities")
    public String citySubmit(@ModelAttribute Location location, Model model) {
        locationRepository.save(location);
        User user = userRepository.findByUserName("kubres");
        List<Location> citiesTo = user.getCitiesTo();
        citiesTo.add(location);
        user.setCitiesTo(citiesTo);
//        userRepository.updateCitiesTo(citiesTo, "kubres");
        model.addAttribute("citiesTo", locationRepository.findAll());
        return "cities";
    }






//    @RequestMapping(value = "/rest/user", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<?> updateUser(@RequestBody User user) {
//
//        User existing = userRepository.read(user.getUserId());
//        copyNonNullProperties(user, existing);
//        userRepository.save(user);
//
//        // ...
//    }
//
//
//    public static void copyNonNullProperties(Object src, Object target) {
//        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
//    }
//
//    public static String[] getNullPropertyNames (Object source) {
//        final BeanWrapper src = new BeanWrapperImpl(source);
//        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
//
//        Set<String> emptyNames = new HashSet<String>();
//        for(java.beans.PropertyDescriptor pd : pds) {
//            Object srcValue = src.getPropertyValue(pd.getName());
//            if (srcValue == null) emptyNames.add(pd.getName());
//        }
//        String[] result = new String[emptyNames.size()];
//        return emptyNames.toArray(result);
//    }

}
