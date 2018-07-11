package com.crypto.raModule.RA.Module.NoSQL.service;

import com.crypto.raModule.RA.Module.NoSQL.entities.CertRequest;
import com.crypto.raModule.RA.Module.NoSQL.entities.Csr;

import java.util.List;

public interface CertRequestService {


    int newCertRequest(Csr csr, String panNumber, int classOfCert, int validityOfCert, String comnts, String subEmail, boolean isEcc);

    List<CertRequest> getAllCertRequestsBySubEmail(String subEmail);

    String getCertRequestsByRaOfficeCode(String raOfficeId);

    List<CertRequest> getAllCertsToBeVerifiedByRaOfficeCode(String raOfficeCode);

    void verifyCertRequest(String raEmail,int id);

    List<CertRequest> getAllCertsToBeAuthByRaOfficeCode(String raEmail,String raOfficeCode);

    void authCertRequest(String name, int id);

    CertRequest getCertReqBySno(int sNo);

    List<CertRequest> getAllCertsByRaOfficeCode(String raOfficeCode);

    String getAllCertsByRaOfficeCodeAndStatus(String raOfficeCode, int status);

    int getNoOfReqByStatus(int certReqStatusPendingWithCaVerf);

    List<CertRequest> getAllCertsInPendingCaVerification();

    void verifyCertRequestByCa(String name, int integer);

    List<CertRequest> getAllCertsInPendingCaAuthorization();

    void authCertRequestByCa(String name, int integer);

    String getAllCertReqs();

    String getAllCertsByStatus(int status);

    String getAllCertsByRaOfficeCodeForCa(String raOfficeCode);

    void caVerifyCertRequest(String name, int sNo);

    void caAuthCertRequest(String name, int sNo);
}
