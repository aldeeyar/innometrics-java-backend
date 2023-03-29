package com.innopolis.innometrics.authserver.service;


import com.innopolis.innometrics.authserver.dto.PageResponse;
import com.innopolis.innometrics.authserver.dto.UserListResponse;
import com.innopolis.innometrics.authserver.dto.UserRequest;
import com.innopolis.innometrics.authserver.dto.UserResponse;
import com.innopolis.innometrics.authserver.entitiy.Permission;
import com.innopolis.innometrics.authserver.entitiy.TemporalToken;
import com.innopolis.innometrics.authserver.entitiy.User;
import com.innopolis.innometrics.authserver.repository.TemporalTokenRepository;
import com.innopolis.innometrics.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final long ONE_MINUTE_IN_MILLIS = 60000;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final TemporalTokenRepository temporalTokenRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public UserListResponse getActiveUsers(String request) {

        request = Objects.equals(request, "") ? null : request;
        List<User> result = userRepository.findAllActive(request);
        UserListResponse response = new UserListResponse();
        for (User u : result) {
            UserResponse temp = new UserResponse();
            temp.setName(u.getName());
            temp.setEmail(u.getEmail());
            temp.setSurname(u.getSurname());
            temp.setIsActive(u.getIsActive());
            temp.setRole(u.getRole().getName());
            response.getUserList().add(temp);
        }
        return response;
    }

    public UserRequest fromUserToUserRequest(User myUser) {
        UserRequest userRequest = new UserRequest();
        userRequest.setName(myUser.getName());
        userRequest.setSurname(myUser.getSurname());
        userRequest.setEmail(myUser.getEmail());
        userRequest.setPassword(myUser.getPassword());
        userRequest.setBirthday(myUser.getBirthday());
        userRequest.setGender(myUser.getGender());
        userRequest.setFacebookAlias(myUser.getFacebookAlias());
        userRequest.setTelegramAlias(myUser.getTelegramAlias());
        userRequest.setTwitterAlias(myUser.getTwitterAlias());
        userRequest.setLinkedinAlias(myUser.getLinkedinAlias());
        userRequest.setIsActive(myUser.getIsActive());
        userRequest.setConfirmedAt(myUser.getConfirmedAt());
        userRequest.setRole(myUser.getRole().getName());
        return userRequest;
    }

    public UserResponse fromUserToUserResponse(User myUser) {
        UserResponse userResponse = new UserResponse();
        userResponse.setName(myUser.getName());
        userResponse.setSurname(myUser.getSurname());
        userResponse.setBirthday(myUser.getBirthday());
        userResponse.setGender(myUser.getGender());
        userResponse.setFacebookAlias(myUser.getFacebookAlias());
        userResponse.setTelegramAlias(myUser.getTelegramAlias());
        userResponse.setTwitterAlias(myUser.getTwitterAlias());
        userResponse.setLinkedinAlias(myUser.getLinkedinAlias());
        userResponse.setEmail(myUser.getEmail());
        userResponse.setIsActive(myUser.getIsActive());
        userResponse.setRole(myUser.getRole().getName());
        List<PageResponse> pages = new ArrayList<>();
        for (Permission permission : myUser.getRole().getPermissions()) {
            PageResponse temp = new PageResponse(permission.getPage().getPage(), permission.getPage().getIcon());
            pages.add(temp);
        }
        userResponse.setPages(pages);
        return userResponse;
    }

    public void sendRessetPasswordEmail(String email, String backUrl) {
        TemporalToken temporalToken = generateNewTokenEnrty(email);
        MimeMessage message = mailSender.createMimeMessage();
        String subject = "InnoMetrics reset password link";
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setFrom("innometrics-notification@innopolis.university");
            helper.setSubject(subject);
            String htmlStr = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org=\n" +
                    "/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                    "<html xmlns=3D\"http://www.w3.org/1999/xhtml\" xml:lang=3D\"en\" lang=3D\"en\">\n" +
                    "  <head>\n" +
                    "    <title></title>\n" +
                    "    <style type=3D\"text/css\">\n" +
                    "      body {margin: 10px; padding: 10px; text-align: left; background-color=\n" +
                    ": #ffffff; font: 13px Arial, Tahoma, Verdana, Helvetica, sans-serif; color:=\n" +
                    " #24272b}\n" +
                    "      td, th {padding: 0; margin: 0; border-bottom: 0px solid #bfd0e6;text-=\n" +
                    "align: left}\n" +
                    "      h4 {display: inline}\n" +
                    "      .size1{width:450px}\n" +
                    "      .size2{width:80px}\n" +
                    "    </style>\n" +
                    "  </head>\n" +
                    "  <body>\n";

            htmlStr += "<h2> Please follow the link to reset password. Be noticed that it will expire in 5 minutes:</h2>\n";
            htmlStr += "<p><br /></p>\n" +
                    "<div> <a href=" + backUrl + "=" + temporalToken.getTemporalToken() + "> Reset password </a>\n" +
                    "   </div>\n" +
                    "   <div>" +
                    "    <br/>\n" +
                    "    Innometrics Team\n" +
                    "    <br/>\n" +
                    "   </div>\n" +
                    "  </body>\n" +
                    "</html>";
            helper.setText(htmlStr, true);
            message.setSender(new InternetAddress("innometrics-notification@innopolis.university"));
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean checkTemporalToken(String email, String tempToken){
        TemporalToken temporalToken = temporalTokenRepository.findByEmailAndTemporalToken(email,tempToken);
        Timestamp dateNow = new Timestamp(System.currentTimeMillis());
        if(temporalToken!=null && temporalToken.getExpirationDate().after(dateNow)){
            temporalTokenRepository.delete(temporalToken);
            return true;
        } else {
            if(temporalToken!=null)
                temporalTokenRepository.delete(temporalToken);
            return false;
        }
    }

    private TemporalToken generateNewTokenEnrty(String email){
        TemporalToken temporalToken = temporalTokenRepository.findByEmail(email);
        if(temporalToken!=null){
            temporalTokenRepository.delete(temporalToken);
        }
        Timestamp dateAfterFiveMinsFromNow = new Timestamp(System.currentTimeMillis() + (5 * ONE_MINUTE_IN_MILLIS));
        temporalToken = new TemporalToken(email, generateNewToken(), dateAfterFiveMinsFromNow);
        return temporalTokenRepository.save(temporalToken);
    }

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
