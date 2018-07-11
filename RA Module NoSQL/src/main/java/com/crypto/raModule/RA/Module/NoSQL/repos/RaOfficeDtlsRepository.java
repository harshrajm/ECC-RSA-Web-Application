package com.crypto.raModule.RA.Module.NoSQL.repos;

import com.crypto.raModule.RA.Module.NoSQL.entities.CertRequest;
import com.crypto.raModule.RA.Module.NoSQL.entities.RaOfficeDtls;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RaOfficeDtlsRepository extends MongoRepository<RaOfficeDtls,String>,QueryDslPredicateExecutor<RaOfficeDtls> {

        public RaOfficeDtls findByRaOfficeCode(String code);

}
