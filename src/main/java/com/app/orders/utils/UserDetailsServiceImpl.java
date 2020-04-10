package com.app.orders.utils;

import com.app.orders.entity.UserDetails;
import com.app.orders.repository.common.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userAuthRepository.findByUsername(username);
        UserDetails byPhone = userAuthRepository.findByPrimaryPhone(username);
        User.UserBuilder builder;
        if (user != null && user.getEnabled()==1) {
            builder = User.withUsername(username);
            builder.password(user.getPassword());
            builder.roles(user.getRole());
        } else if (byPhone!=null && byPhone.getEnabled()==1){
            builder = User.withUsername(username);
            builder.password(byPhone.getPassword());
            builder.roles(byPhone.getRole());
        }
        else {
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }
}
