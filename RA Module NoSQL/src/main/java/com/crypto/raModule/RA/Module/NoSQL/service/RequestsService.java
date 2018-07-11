package com.crypto.raModule.RA.Module.NoSQL.service;

import com.crypto.raModule.RA.Module.NoSQL.entities.RaOffice;
import com.crypto.raModule.RA.Module.NoSQL.entities.Requests;

public interface RequestsService {

    boolean isSubscriberEmailIdPresent(String email);

    Requests getRequestDtlsBySubEmail(String subEmail);

    void addNewRequest(String raEmail,String subEmail);

    boolean isOtpMatching(String subOTP, String subEmail);

    RaOffice getSubRaOffice(String subEmail);
}
