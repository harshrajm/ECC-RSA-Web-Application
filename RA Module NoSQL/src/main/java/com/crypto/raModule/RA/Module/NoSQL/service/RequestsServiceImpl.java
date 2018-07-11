package com.crypto.raModule.RA.Module.NoSQL.service;

import com.crypto.raModule.RA.Module.NoSQL.entities.QRequests;
import com.crypto.raModule.RA.Module.NoSQL.entities.RaOffice;
import com.crypto.raModule.RA.Module.NoSQL.entities.Requests;
import com.crypto.raModule.RA.Module.NoSQL.entities.User;
import com.crypto.raModule.RA.Module.NoSQL.repos.RequestsRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class RequestsServiceImpl implements RequestsService {

    @Autowired
    RequestsRepository requestsRepository;

    @Autowired
    UserService userService;

    @Override
    public boolean isSubscriberEmailIdPresent(String email) {
        //User user = userService.getUserByEmail(raEmail);
       QRequests qRequests = new QRequests("req");
        com.querydsl.core.types.dsl.BooleanExpression filterByEmail = qRequests.subEmail.eq(email);
        //com.querydsl.core.types.dsl.BooleanExpression filterByRaOfficeCode = qRequests.raOffice.raOfficeCode.eq(user.getRaOffice().getRaOfficeCode());
        List<Requests> requests = (List<Requests>) requestsRepository.findAll(filterByEmail);

        if (requests.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public Requests getRequestDtlsBySubEmail(String subEmail) {

        QRequests qRequests = new QRequests("req");
        com.querydsl.core.types.dsl.BooleanExpression filterByEmail = qRequests.subEmail.eq(subEmail);
        return ((List<Requests>) requestsRepository.findAll(filterByEmail)).get(0);
    }

    @Override
    public void addNewRequest(String raEmail, String subEmail) {

        User user = userService.getUserByEmail(raEmail);


        Requests requests = new Requests();
        requests.setRaId(raEmail);
        requests.setSubEmail(subEmail);
        requests.setOtp(String.format("%04d", new Random().nextInt(10000)));
        requests.setMailSentOn(new Date());
        requests.setFirstInsert(new Date());
        //requests.setReqRaised(false);
        requests.setRaOffice(user.getRaOffice());
        requestsRepository.save(requests);



    }

    @Override
    public boolean isOtpMatching(String subOTP, String subEmail) {

        QRequests qRequests = new QRequests("requests");
        BooleanExpression filterBySubEmail = qRequests.subEmail.eq(subEmail);

        Requests request = ((List<Requests>) requestsRepository.findAll(filterBySubEmail)).get(0);

        if (request.getOtp().equals(subOTP)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public RaOffice getSubRaOffice(String subEmail) {
        QRequests qRequests = new QRequests("req");
        com.querydsl.core.types.dsl.BooleanExpression filterByEmail = qRequests.subEmail.eq(subEmail);
        return ((List<Requests>) requestsRepository.findAll(filterByEmail)).get(0).getRaOffice();    }

}
