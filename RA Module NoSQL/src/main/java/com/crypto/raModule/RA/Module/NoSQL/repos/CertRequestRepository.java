package com.crypto.raModule.RA.Module.NoSQL.repos;

import com.crypto.raModule.RA.Module.NoSQL.entities.CertRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface CertRequestRepository extends MongoRepository<CertRequest,String>,QueryDslPredicateExecutor<CertRequest>,PagingAndSortingRepository<CertRequest,String> {

    //CertRequest findById(String id);

    Optional<CertRequest> findById(Predicate predicate);
}
