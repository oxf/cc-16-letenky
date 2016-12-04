
package cz.codecamp;

import cz.codecamp.model.Flight;
import cz.codecamp.model.Location;
import cz.codecamp.model.User;
import cz.codecamp.repository.UserRepository;
import cz.codecamp.services.EmailService;
import cz.codecamp.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.annotation.Rollback;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@EnableScheduling
@Configuration
@SpringBootApplication
@Rollback(value = false)
public class LetenkyApplication {

    @Value("${email.from}")
    private String email;

    @Value("${password}")
    private String password;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FlightService flightService;

    @Autowired
    EmailService emailService;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("cs", "cz"));

	private static final Logger log = LoggerFactory.getLogger(LetenkyApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LetenkyApplication.class);
	}

    @Bean
    public User saveUser() throws ParseException {
        User user = new User();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateFromString = "05.12.2016";
        String dateToString = "01.01.2017";
        Date dateFrom = dateFormat.parse(dateFromString);
        Date dateTo = dateFormat.parse(dateToString);

        user.setCityFrom(new Location("Prague", "Czech republic", "prague_cz"));
        List<Location> citiesTo = new ArrayList<Location>();
        citiesTo.add(new Location("Vienna", "Austria", "vienna_at"));
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
        return user;
    }

    @Bean
    public List<Flight> testJson() throws IOException {
        return flightService.saveFlightsFromJson();
    }

    @Bean
    public List<Flight> testFlights() {
        List<Flight> flights = flightService.getFlightsForUser("kubres@gmail.com");
        System.out.println(flights);
        return flights;
    }

    @Bean
    public Properties properties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return properties;
    }


    @Bean
    public Message getMessage(Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setSubject("Letenky dle Vašich přání " + LocalDate.now().format(timeFormatter));
        return message;
    }

    @Bean
    public Session getSession(Properties properties) {

        return Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
    }

    @Bean
    @Qualifier("google")
    public HttpHeaders httpGoogleHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    @Bean
    @Qualifier("google")
    public HttpEntity<String> httpGoogleEntity(@Qualifier("google") HttpHeaders headers) {
        return new HttpEntity<>("parameters", headers);
    }

    @Bean
    public String testEmail() throws IOException, MessagingException {
        emailService.sendEmailsToUsers();
        return "Done";
    }

//	@Bean
//	public CommandLineRunner demo(FlightRepository flightRepository, UserRepository userRepository) {
//		return (args) -> {
//
//			RestTemplate restTemplate = new RestTemplate();
//			Query query = new Query();
//			String queryString = query.buildQuery();
//			ResponseEntity<String> response = restTemplate.getForEntity(queryString, String.class);
//			ObjectMapper mapper = new ObjectMapper();
//			JsonNode root = mapper.readTree(response.getBody());
//			JsonNode data = root.get("data");
//			for (JsonNode jflight : data) {
//
//				JsonNode jflyDuration = jflight.get("fly_duration");
//				JsonNode jprice = jflight.get("conversion").get("EUR");
//				JsonNode jnightsInDest = jflight.get("nightsInDest");
//				JsonNode jcityTo = jflight.get("cityTo");
//				JsonNode jcityFrom = jflight.get("cityFrom");
//				JsonNode jdTimeStamp = jflight.get("dTimeUTC");
//
//				String flyDuration = mapper.treeToValue(jflyDuration, String.class);
//				Integer price = mapper.treeToValue(jprice, Integer.class);
//				Integer nightsInDest = mapper.treeToValue(jnightsInDest, Integer.class);
//				String cityTo = mapper.treeToValue(jcityTo, String.class);
//				String cityFrom = mapper.treeToValue(jcityFrom, String.class);
//				Integer dTimeStamp = mapper.treeToValue(jdTimeStamp, Integer.class);
//
//				Flight flight = new Flight(cityFrom, cityTo, price, nightsInDest, flyDuration, dTimeStamp);
//				log.info(flight.getFlyDurationMinutes().toString());
//				flightRepository.save(flight);
//
//
//				log.info("Flights found with findAll():");
//				log.info("-------------------------------");
//				for (Flight flight1 : flightRepository.findAll()) {
//					log.info(flight1.toString());
//				}
//				log.info("");
//
//
//				Flight flight2 = flightRepository.findOne(1L);
//				log.info("Flight found with findOne(1L):");
//				log.info("--------------------------------");
//				log.info(flight2.toString());
//				log.info("");
//
//
//				log.info("Flight found with findByCityFromAndCityTo(Brno):");
//				log.info("--------------------------------------------");
//				for (Flight brno : flightRepository.findByCityFromAndCityTo("Brno", "Nis")) {
//					log.info(brno.toString());
//				}
//				log.info("");
//
//				log.info("Flights fitting criteria for a user:");
//				log.info("--------------------------------------------");
//			}
//		};
//	};


}
