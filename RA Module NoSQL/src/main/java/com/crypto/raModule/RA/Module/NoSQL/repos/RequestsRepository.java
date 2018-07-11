package com.crypto.raModule.RA.Module.NoSQL.repos;

import com.crypto.raModule.RA.Module.NoSQL.entities.Requests;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface RequestsRepository extends MongoRepository<Requests,String>,QueryDslPredicateExecutor<Requests> {

}
