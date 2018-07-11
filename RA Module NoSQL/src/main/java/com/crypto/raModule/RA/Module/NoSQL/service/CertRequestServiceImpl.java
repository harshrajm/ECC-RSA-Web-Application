package com.crypto.raModule.RA.Module.NoSQL.service;

import com.crypto.raModule.RA.Module.NoSQL.entities.RaOffice;
import com.crypto.raModule.RA.Module.NoSQL.entities.Csr;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.crypto.raModule.RA.Module.NoSQL.entities.*;
import com.crypto.raModule.RA.Module.NoSQL.helper.AppConstants;
import com.crypto.raModule.RA.Module.NoSQL.repos.CertRequestRepository;
import com.mongodb.BasicDBObject;
import com.querydsl.core.types.Predicate;
import javafx.beans.binding.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class CertRequestServiceImpl implements CertRequestService {

    @Autowired
    RequestsService requestsService;

    @Autowired
    CertRequestRepository certRequestRepository;

    @Autowired
    NextSequenceService nextSequenceService;


    @Autowired
    MongoOperations mongoOperations;

    @Override
    public int newCertRequest(Csr csr, String panNumber, int classOfCert,
                              int validityOfCert, String comnts, String subEmail, boolean isEcc) {

        RaOffice subRaOffice = requestsService.getSubRaOffice(subEmail);
        int seq = nextSequenceService.getNextSequence("customSequences");
        CertRequest certRequest = new CertRequest();
        certRequest.setsNo(seq);
        certRequest.setCsr(csr);
        certRequest.setCertClass(classOfCert);
        certRequest.setValidity(validityOfCert);
        certRequest.setPan(panNumber.toUpperCase());
        certRequest.setOther(comnts);
        certRequest.setEcc(isEcc);
        certRequest.setSubEmail(subEmail);
        certRequest.setRaOffice(subRaOffice);
        certRequest.setReqDate(new Date());
        certRequest.setStatus(AppConstants.CERT_REQ_STATUS_PENDING_VERF);
        certRequestRepository.save(certRequest);

        return seq;
    }

    @Override
    public List<CertRequest> getAllCertRequestsBySubEmail(String subEmail) {

        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression filterBySubEmail = qCertRequest.subEmail.eq(subEmail);
        return (List<CertRequest>) certRequestRepository.findAll(filterBySubEmail);
    }

    @Override
    public String getCertRequestsByRaOfficeCode(String raOfficeId) {

        Criteria find = Criteria.where("raOffice.raOfficeCode").is(raOfficeId);
        Query query = new Query().addCriteria(find).with(new Sort(Sort.Direction.DESC, "sNo")).with(new PageRequest(0, AppConstants.DATA_LOAD_SIZE));
        List<BasicDBObject> result = mongoOperations.find(query, BasicDBObject.class, "CertRequest");
        return result.toString();
    }

    @Override
    public List<CertRequest> getAllCertsToBeVerifiedByRaOfficeCode(String raOfficeCode) {

        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression filterByStatus = qCertRequest.status.eq(AppConstants.CERT_REQ_STATUS_PENDING_VERF);
        com.querydsl.core.types.dsl.BooleanExpression filterByRaOfficeCode = qCertRequest.raOffice.raOfficeCode.eq(raOfficeCode);
        return (List<CertRequest>) certRequestRepository.findAll(filterByStatus.and(filterByRaOfficeCode));
    }

    @Override
    public void verifyCertRequest(String raEmail, int id) {

        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression booleanExpression = qCertRequest.sNo.eq(id);
        CertRequest certRequest = ((List<CertRequest>) certRequestRepository.findAll(booleanExpression)).get(0);
        if (certRequest.getStatus() == AppConstants.CERT_REQ_STATUS_PENDING_VERF) {
            certRequest.setVerifiedByRaEmail(raEmail);
            certRequest.setVerifiedByRaOn(new Date());
            certRequest.setStatus(AppConstants.CERT_REQ_STATUS_PENDING_AUTH);
            certRequestRepository.save(certRequest);
        }
    }

    @Override
    public List<CertRequest> getAllCertsToBeAuthByRaOfficeCode(String raEmail,String raOfficeCode) {
        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression filterByStatus = qCertRequest.status.eq(AppConstants.CERT_REQ_STATUS_PENDING_AUTH);
        com.querydsl.core.types.dsl.BooleanExpression filterByRaOfficeCode = qCertRequest.raOffice.raOfficeCode.eq(raOfficeCode);

        List<CertRequest> certRequests = (List<CertRequest>) certRequestRepository.findAll(filterByStatus.and(filterByRaOfficeCode));
        Iterator<CertRequest> itr = certRequests.iterator();
        while (itr.hasNext()) {
            CertRequest certRequestItem = itr.next();
            if (raEmail.equalsIgnoreCase(certRequestItem.getVerifiedByRaEmail())) {
                itr.remove();
            }
        }
        return certRequests;
    }

    @Override
    public void authCertRequest(String raEmail, int id) {
        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression booleanExpression = qCertRequest.sNo.eq(id);
        CertRequest certRequest = ((List<CertRequest>) certRequestRepository.findAll(booleanExpression)).get(0);
        if (certRequest.getStatus() == AppConstants.CERT_REQ_STATUS_PENDING_AUTH) {
            certRequest.setAuthByRaEmail(raEmail);
            certRequest.setAuthByRaOn(new Date());
            certRequest.setStatus(AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_VERF);
            certRequestRepository.save(certRequest);
        }
    }

    @Override
    public CertRequest getCertReqBySno(int sNo) {
        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression booleanExpression = qCertRequest.sNo.eq(Integer.valueOf(sNo));
        return ((List<CertRequest>) certRequestRepository.findAll(booleanExpression)).get(0);
    }

    @Override
    public List<CertRequest> getAllCertsByRaOfficeCode(String raOfficeCode) {
        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression filterByRaOfficeCode = qCertRequest.raOffice.raOfficeCode.eq(raOfficeCode);

        return (List<CertRequest>) certRequestRepository.findAll(filterByRaOfficeCode);
    }

    @Override
    public String getAllCertsByRaOfficeCodeAndStatus(String raOfficeCode, int status) {

        Criteria find = Criteria.where("raOffice.raOfficeCode").is(raOfficeCode).and("status").is(status);
        Query query = new Query().addCriteria(find).with(new Sort(Sort.Direction.DESC, "sNo"))/*.with(new PageRequest(0, AppConstants.DATA_LOAD_SIZE))*/;
        List<BasicDBObject> result = mongoOperations.find(query, BasicDBObject.class, "CertRequest");
        return result.toString();
        //return null;
    }

    @Override
    public int getNoOfReqByStatus(int certReqStatus) {
        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression filterByStatus = qCertRequest.status.eq(certReqStatus);
        List<CertRequest> certRequests = (List<CertRequest>) certRequestRepository.findAll(filterByStatus);

        if(certRequests.isEmpty()){
            return 0;
        }else {
            return certRequests.size();
        }
    }

    @Override
    public List<CertRequest> getAllCertsInPendingCaVerification() {

        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression filterByStatus = qCertRequest.status.eq(AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_VERF);
        List<CertRequest> certRequests = (List<CertRequest>) certRequestRepository.findAll(filterByStatus);
        return certRequests;
    }

    @Override
    public void verifyCertRequestByCa(String caEmail, int id) {

        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression booleanExpression = qCertRequest.sNo.eq(id);
        CertRequest certRequest = ((List<CertRequest>) certRequestRepository.findAll(booleanExpression)).get(0);
        if (certRequest.getStatus() == AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_VERF) {
            //certRequest.setVerifiedByRaEmail(raEmail);
            certRequest.setVerifiedByCaEmail(caEmail);
            certRequest.setVerifiedByCaOn(new Date());
            certRequest.setStatus(AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_AUTH);
            certRequestRepository.save(certRequest);
        }

    }

    @Override
    public List<CertRequest> getAllCertsInPendingCaAuthorization() {
        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression filterByStatus = qCertRequest.status.eq(AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_AUTH);
        List<CertRequest> certRequests = (List<CertRequest>) certRequestRepository.findAll(filterByStatus);
        return certRequests;
    }

    @Override
    public void authCertRequestByCa(String caEmail, int id) {
        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression booleanExpression = qCertRequest.sNo.eq(id);
        CertRequest certRequest = ((List<CertRequest>) certRequestRepository.findAll(booleanExpression)).get(0);
        if (certRequest.getStatus() == AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_AUTH) {
            //certRequest.setVerifiedByRaEmail(raEmail);
            certRequest.setAuthByCaEmail(caEmail);
            certRequest.setAuthByCaOn(new Date());
            certRequest.setStatus(AppConstants.CERT_REQ_STATUS_CERT_READY_TO_GENERATE);
            certRequestRepository.save(certRequest);
        }
    }

    @Override
    public String getAllCertReqs() {
        //Criteria find = Criteria.where("raOffice.raOfficeCode").is(raOfficeId);
        Query query = new Query().with(new Sort(Sort.Direction.DESC, "sNo")).with(new PageRequest(0, AppConstants.DATA_LOAD_SIZE));
        List<BasicDBObject> result = mongoOperations.find(query, BasicDBObject.class, "CertRequest");
        return result.toString();
    }

    @Override
    public String getAllCertsByStatus(int status) {
        Criteria find = Criteria.where("status").is(status);
        Query query = new Query().addCriteria(find).with(new Sort(Sort.Direction.DESC, "sNo"))/*.with(new PageRequest(0, AppConstants.DATA_LOAD_SIZE))*/;
        List<BasicDBObject> result = mongoOperations.find(query, BasicDBObject.class, "CertRequest");
        return result.toString();
    }

    @Override
    public String getAllCertsByRaOfficeCodeForCa(String raOfficeCode) {
        Criteria find = Criteria.where("raOffice.raOfficeCode").is(raOfficeCode);
        Query query = new Query().addCriteria(find).with(new Sort(Sort.Direction.DESC, "sNo"))/*.with(new PageRequest(0, AppConstants.DATA_LOAD_SIZE))*/;
        List<BasicDBObject> result = mongoOperations.find(query, BasicDBObject.class, "CertRequest");
        return result.toString();
    }

    @Override
    public void caVerifyCertRequest(String caEmail, int id) {
        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression booleanExpression = qCertRequest.sNo.eq(id);
        CertRequest certRequest = ((List<CertRequest>) certRequestRepository.findAll(booleanExpression)).get(0);
        if (certRequest.getStatus() == AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_VERF) {
            //certRequest.setVerifiedByRaEmail(raEmail);
            certRequest.setVerifiedByCaEmail(caEmail);
            certRequest.setVerifiedByCaOn(new Date());
            certRequest.setStatus(AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_AUTH);
            certRequestRepository.save(certRequest);
        }
    }

    @Override
    public void caAuthCertRequest(String caEmail, int id) {
        QCertRequest qCertRequest = new QCertRequest("req");
        com.querydsl.core.types.dsl.BooleanExpression booleanExpression = qCertRequest.sNo.eq(id);
        CertRequest certRequest = ((List<CertRequest>) certRequestRepository.findAll(booleanExpression)).get(0);
        if (certRequest.getStatus() == AppConstants.CERT_REQ_STATUS_PENDING_WITH_CA_AUTH) {
            //certRequest.setVerifiedByRaEmail(raEmail);
            certRequest.setAuthByCaEmail(caEmail);
            certRequest.setAuthByCaOn(new Date());
            certRequest.setStatus(AppConstants.CERT_REQ_STATUS_CERT_READY_TO_GENERATE);
            certRequestRepository.save(certRequest);
        }
    }


}
