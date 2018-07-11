package com.crypto.raModule.RA.Module.NoSQL.controller;

import com.crypto.raModule.RA.Module.NoSQL.entities.CaHomeRaOfficeDtlsHelper;
import com.crypto.raModule.RA.Module.NoSQL.entities.CertRequest;
import com.crypto.raModule.RA.Module.NoSQL.entities.User;
import com.crypto.raModule.RA.Module.NoSQL.helper.AppConstants;
import com.crypto.raModule.RA.Module.NoSQL.service.CertRequestService;
import com.crypto.raModule.RA.Module.NoSQL.service.UserService;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class RestCtrl {

    @Autowired
    UserService userService;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    CertRequestService certRequestService;

    @RequestMapping("/rest/getSubCertRequests/{page}")
public String getSubCertRequests(Principal principal, @PathVariable int page){

        System.out.println("Rest!! page "+page);
        User user = userService.getUserByEmail(principal.getName());
        System.out.println(user.getEmail());

        Criteria find = Criteria.where("raOffice.raOfficeCode").is(user.getRaOffice().getRaOfficeCode());
        Query query = new Query().addCriteria(find).with(new Sort(Sort.Direction.DESC, "sNo")).with(new PageRequest(page, AppConstants.DATA_LOAD_SIZE));
        List<BasicDBObject> result = mongoOperations.find(query, BasicDBObject.class, "CertRequest");

        return result.toString();
    }


    @RequestMapping("/rest/loadDataOfRaOfficeByCode/{raOfficeCode}")
    public CaHomeRaOfficeDtlsHelper loadDataOfRaOfficeByCode(Principal principal, @PathVariable String raOfficeCode){
        System.out.println("---------loadDataOfRaOfficeByCode-----------");

        List<CertRequest> certRequests = certRequestService.getAllCertsByRaOfficeCode(raOfficeCode);

        int totalReq = 0;
        int penRaVer = 0;
        int penRaAuth = 0;
        int penCaVer = 0;
        int penCaAuth = 0;
        int readyToGen = 0;
        int readyToDnld = 0;
        int cncld = 0;

        totalReq = certRequests.size();

        for (CertRequest certRequest : certRequests) {

            switch (certRequest.getStatus()) {
                case AppConstants.CERT_REQ_STATUS_PENDING_VERF:
                    penRaVer++;
                    break;
                case AppConstants.CERT_REQ_STATUS_PENDING_AUTH:
                    penRaAuth++;
                    break;
                case AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_VERF:
                    penCaVer++;
                    break;
                case AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_AUTH:
                    penCaAuth++;
                    break;
                case AppConstants.CERT_REQ_STATUS_CERT_READY_TO_GENERATE:
                    readyToGen++;
                    break;
                case AppConstants.CERT_REQ_STATUS_CERT_READY_FOR_DOWNLOAD:
                    readyToDnld++;
                    break;
                case AppConstants.CERT_REQ_STATUS_CERT_CANCELLED:
                    cncld++;
                    break;
            }


        }

        CaHomeRaOfficeDtlsHelper helper = new CaHomeRaOfficeDtlsHelper();
        helper.setTotalRequests(totalReq);
        helper.setPendingRaVerification(penRaVer);
        helper.setPendingRaAuthorization(penRaAuth);
        helper.setPendingCaVerification(penCaVer);
        helper.setPendingCaAuthorization(penCaAuth);
        helper.setReadyToGenerate(readyToGen);
        helper.setCertificateReadyToDownload(readyToDnld);
        helper.setCancelled(cncld);


        return helper;
    }


    @RequestMapping("/rest/getSubCertRequestsForCa/{page}")
    public String getSubCertRequestsForCa( @PathVariable int page){

        System.out.println("Rest!! page getSubCertRequestsForCa "+page);
        //User user = userService.getUserByEmail(principal.getName());
        //System.out.println(user.getEmail());

        /*Criteria find = Criteria.where("raOffice.raOfficeCode").is(user.getRaOffice().getRaOfficeCode());
        Query query = new Query().addCriteria(find).with(new Sort(Sort.Direction.DESC, "sNo")).with(new PageRequest(page, AppConstants.DATA_LOAD_SIZE));
        List<BasicDBObject> result = mongoOperations.find(query, BasicDBObject.class, "CertRequest");*/

        Query query = new Query().with(new Sort(Sort.Direction.DESC, "sNo")).with(new PageRequest(page, AppConstants.DATA_LOAD_SIZE));
        List<BasicDBObject> result = mongoOperations.find(query, BasicDBObject.class, "CertRequest");

        return result.toString();
    }
}
