package com.crypto.raModule.RA.Module.NoSQL.controller;

import com.crypto.raModule.RA.Module.NoSQL.entities.*;
import com.crypto.raModule.RA.Module.NoSQL.helper.AppConstants;
import com.crypto.raModule.RA.Module.NoSQL.repos.CertRequestRepository;
import com.crypto.raModule.RA.Module.NoSQL.repos.RaOfficeDtlsRepository;
import com.crypto.raModule.RA.Module.NoSQL.repos.RequestsRepository;
import com.crypto.raModule.RA.Module.NoSQL.repos.UserRepository;
import com.crypto.raModule.RA.Module.NoSQL.service.RequestsService;
import com.crypto.raModule.RA.Module.NoSQL.service.UserService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Principal;
import java.util.*;


@RequestMapping("/test")
@RestController
public class TestCtrl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestsRepository requestsRepository;

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private  CertRequestRepository certRequestRepository;

    @Autowired
    private RaOfficeDtlsRepository raOfficeDtlsRepository;



    @GetMapping("/sampleInsert")
    public String sampleInsert() {

        CertDetails certDetails = new CertDetails("15613456124", new Date(), new Date());

        User user = new User("Abhinay", "7702028128", "abhinayra@email.com",
                new BCryptPasswordEncoder().encode("password"), AppConstants.ROLE_RA_OFFICER, true, Arrays.asList(certDetails),
                new RaOffice("RAHYD1", "Masab Tank Hyd", "040-1512313"));

        userRepository.save(user);
        /*CertDetails certDetails1 = new CertDetails("15163285334", new Date(), new Date());
        User user1 = new User("Nethaji", "7702028126", "netagca@email.com",
                new BCryptPasswordEncoder().encode("password"), AppConstants.ROLE_RA_OFFICER, false, Arrays.asList(certDetails1),
                new RaOffice("RAHYD1", "Masab Tank Hyd", "040-1512313"));

        userRepository.save(user1);*/
        return "inserted!";
    }

    @GetMapping("/getDa")
    public User getDa() {

        return userService.getUserByEmail("harshrajm@gmail.com");
    }

    @GetMapping("/getUsrs")
    public List<User> gtUsr() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('RA_ADMIN')")
    @GetMapping("/addCert")
    public List<User> addCert() {


        User user = userService.getUserByEmail("harshrajms@gmail.com");
        user.getCertDetails().add(new CertDetails("69626969", new Date(), new Date()));
        userRepository.save(user);
        return userRepository.findAll();
    }


    @GetMapping("/getPh")
    public Iterable<User> getbyPh() {


        QUser qUser = new QUser("user");
        BooleanExpression filterByContact = qUser.contactNo.eq("7702028125");

        return userRepository.findAll(filterByContact);

    }




    @GetMapping("/insReq")
    String insReq(Principal principal) {

        Requests requests = new Requests();
        requests.setRaId(principal.getName());
        requests.setSubEmail("sub4@gmail.com");
        requests.setOtp(String.format("%04d", new Random().nextInt(10000)));
        requests.setMailSentOn(new Date());
        requests.setFirstInsert(new Date());
        //requests.setReqRaised(false);

        requestsRepository.save(requests);

        return "inserted /n" + requestsRepository.findAll();
    }


    @GetMapping("/ifemailreg")
    public String ifemailreg() {

        return String.valueOf(requestsService.isSubscriberEmailIdPresent("sub11@gmail.com"));
    }

    @GetMapping("/testMail")
    public String testMAil() throws Exception {

        //notificationService.sendMail();

        final String username = "eccapp";
        final String password = "Ecc@123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "mail.idrbt.ac.in");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "*");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {


            String otp = "1545";
            String email = "qwerty@qwerty.com";

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("eccapp@idrbt.ac.in"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("hrmdoget@gmail.com"));
            message.setSubject("OTP to login in ECC APP");
            message.setContent("<p>Hi,</p><br/>\n" +
                    "<p>Your Email ID <strong>" + email + "</strong> has been successfully registered in ECC App.</p>\n" +
                    "<p>Use the OTP <strong>" + otp + "</strong> to request or track certificate. </p>\n" +
                    "<br/>\n" +
                    "<p>This an auto generated Email from ECC App, do not reply to this mail. You are getting this mail because you tried to log into ECC App at " + new Date() + " </p>", "text/html");

            Transport.send(message);
            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return "mail sent";
    }

    @GetMapping("/pageable")
    public List<CertRequest> testPagination() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "sNo"));
        Pageable pageable = new PageRequest(1, 10, sort);
        return  certRequestRepository.findAll(sort);

    }

    @GetMapping("/insRaOffice")
    public String insRaOffice(){

        for(int i = 2;i<=10;i++){
            RaOfficeDtls raOfficeDtls = new RaOfficeDtls();
            raOfficeDtls.setRaOfficeCode("RAHYD"+i);
            raOfficeDtls.setAddress("Area"+i+", road"+i+", Hyd");
            raOfficeDtls.setContactNo("040-151233"+i);
            raOfficeDtlsRepository.save(raOfficeDtls);

        }


        return "done";

    };

    @GetMapping("/getRaOffice")
    public List<RaOfficeDtls> getRaOffice(){
       return raOfficeDtlsRepository.findAll();
    }

    @GetMapping("/insert123")
    public String insertRaAdmins(){

        for (int i = 1 ; i<=5;i++){
            RaOfficeDtls raOfficeDtls = new RaOfficeDtls();
            raOfficeDtls.setRaOfficeCode("RAMUM"+i);
            raOfficeDtls.setAddress("Area"+i+", road"+i+", MUM");
            raOfficeDtls.setContactNo("022-151233"+i);
            //raOfficeDtlsRepository.save(raOfficeDtls);
            CertDetails certDetails = new CertDetails();
            certDetails.setCertSerialNo("31585353"+i);
            certDetails.setValidFrm(new Date());
            certDetails.setValidTo(new Date());

            User user = new User("raUserO"+i, "770202812"+i, "ra_ofcr"+i+"@email.com",
                    new BCryptPasswordEncoder().encode("password"), AppConstants.ROLE_RA_OFFICER, false, Arrays.asList(certDetails),
                    new RaOffice(raOfficeDtls.getRaOfficeCode(),raOfficeDtls.getAddress(),raOfficeDtls.getContactNo()));
            userRepository.save(user);

        }
        return "done";
    }


    @GetMapping("/delUnwanted")
    public String delUnwanted(){

        List<User> users = userRepository.findAll();

        for(User x :users){
            if(x.getEmail().contains("raAdmin"))
                userRepository.delete(x);
        }

        return "done";
    }


}
