package cz.codecamp.services;

import cz.codecamp.model.Flight;
import cz.codecamp.model.User;
import cz.codecamp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FlightService flightService;

    private final Message message;

    private final TemplateEngine templateEngine;

    private Context context = new Context(new Locale("cs", "cz"));

    @Autowired
    public EmailServiceImpl(Message message, TemplateEngine templateEngine) {
        this.message = message;
        this.templateEngine = templateEngine;
    }

    @Scheduled(fixedRate = 86400000) //24 hours cycle
    public void sendEmailsToUsers() throws MessagingException {
        Iterable<User> users = userRepository.findAll();
        for (User user : users){
            String emailLogin = user.getEmailLogin();
            List<Flight> flights = flightService.getFlightsForUser(emailLogin);
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailLogin));
            message.setContent(buildLunchMenuList(flights), "text/html; charset=utf-8");
            Transport.send(message);
        }
    }

    private String buildLunchMenuList(List<Flight> flights) throws MessagingException {
        context.setVariable("name", "Letenky dle Vašich přání");
        context.setVariable("date", new Date());
        context.setVariable("flights", flights);
        return templateEngine.process("email-template", context);
    }

}
