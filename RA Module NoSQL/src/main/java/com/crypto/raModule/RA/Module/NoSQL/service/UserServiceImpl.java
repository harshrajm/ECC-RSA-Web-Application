package com.crypto.raModule.RA.Module.NoSQL.service;

import com.crypto.raModule.RA.Module.NoSQL.entities.QUser;
import com.crypto.raModule.RA.Module.NoSQL.entities.User;
import com.crypto.raModule.RA.Module.NoSQL.helper.AppConstants;
import com.crypto.raModule.RA.Module.NoSQL.repos.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User getUserByEmail(String email) {

        QUser qUser = new QUser("user");
        BooleanExpression filterByEmail = qUser.email.eq(email);
        List<User> users = (List<User>) userRepository.findAll(filterByEmail);
        if(!users.isEmpty())
        return users.get(0);
        else return null;
    }

    @Override
    public String getUserRaOfficeCode(String email) {
        QUser qUser = new QUser("user");
        BooleanExpression filterByEmail = qUser.email.eq(email);
        List<User> users = (List<User>) userRepository.findAll(filterByEmail);
        return users.get(0).getRaOffice().getRaOfficeCode();
    }

    @Override
    public boolean isMoreThanOneRaAdminInRaOffice(String raOfficeCode) {

        QUser qUser = new QUser("usr");
        BooleanExpression filterByAdmin = qUser.admin.eq(true);
        BooleanExpression filterByRole = qUser.role.eq(AppConstants.ROLE_RA_OFFICER);
        BooleanExpression filterByRAOfcCode = qUser.raOffice.raOfficeCode.eq(raOfficeCode);

        List<User> users = (List<User>) userRepository.findAll(filterByAdmin.and(filterByRole).and(filterByRAOfcCode));
        System.out.println("list size ----> "+users.size());
        if (!users.isEmpty()) {
            if (users.size() > 1) {
                return true;
            }
        }
        return false;


    }
}
