package com.crypto.raModule.RA.Module.NoSQL.helper;

public class AppConstants {

    public static final int ROLE_RA_OFFICER = 1;
    public static final int ROLE_CA_OFFICER = 2;

    public static final  int CERT_REQ_STATUS_PENDING_VERF = 0;
    public static final  int CERT_REQ_STATUS_PENDING_AUTH = 1;
    public static final  int CERT_REQ_STATUS_PENDING_WITH_CA_VERF = 2;
    public static final  int CERT_REQ_STATUS_PENDING_WITH_CA_AUTH = 3;
    public static final  int CERT_REQ_STATUS_CERT_READY_TO_GENERATE = 4;
    public static final  int CERT_REQ_STATUS_CERT_READY_FOR_DOWNLOAD = 5;

    public static final  int CERT_REQ_STATUS_CERT_CANCELLED = 9;

    public static final int DATA_LOAD_SIZE = 20;
}
