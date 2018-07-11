package com.crypto.raModule.RA.Module.NoSQL.repos;

import com.crypto.raModule.RA.Module.NoSQL.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface UserRepository extends MongoRepository<User, String> ,QueryDslPredicateExecutor<User>{



}
