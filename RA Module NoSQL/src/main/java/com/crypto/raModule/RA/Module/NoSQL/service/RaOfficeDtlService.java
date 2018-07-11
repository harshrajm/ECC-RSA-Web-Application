package com.crypto.raModule.RA.Module.NoSQL.service;

import com.crypto.raModule.RA.Module.NoSQL.entities.RaOfficeDtls;

import java.util.List;

public interface RaOfficeDtlService {


    List<RaOfficeDtls> loadAllRaOfficeCodes();
}
