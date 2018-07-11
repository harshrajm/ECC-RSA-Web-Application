package com.crypto.raModule.RA.Module.NoSQL.service;

import com.crypto.raModule.RA.Module.NoSQL.entities.QRaOfficeDtls;
import com.crypto.raModule.RA.Module.NoSQL.entities.RaOfficeDtls;
import com.crypto.raModule.RA.Module.NoSQL.repos.RaOfficeDtlsRepository;
import javafx.beans.binding.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaOfficeDtlServiceImpl implements RaOfficeDtlService {

    @Autowired
    RaOfficeDtlsRepository raOfficeDtlsRepository;

    @Override
    public List<RaOfficeDtls> loadAllRaOfficeCodes() {

        return raOfficeDtlsRepository.findAll();
    }
}
