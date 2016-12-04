package cz.codecamp.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

@Service
public interface EmailService {

    @Scheduled(fixedRate = 86400000) //24 hours cycle
    void sendEmailsToUsers() throws MessagingException;

}
