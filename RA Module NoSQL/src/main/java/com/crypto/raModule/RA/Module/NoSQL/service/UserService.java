package com.crypto.raModule.RA.Module.NoSQL.service;

import com.crypto.raModule.RA.Module.NoSQL.entities.User;

import java.util.List;

public  interface UserService {

    User getUserByEmail(String email);

    String getUserRaOfficeCode(String name);

    boolean isMoreThanOneRaAdminInRaOffice(String raOfficeCode);

}
