package it.fjacquin.ppmtool.services;

import it.fjacquin.ppmtool.domain.User;
import it.fjacquin.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser (User newUser){

        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

        //Username has to be unique (exception)
        
        //Make sure that password and confirm Password match
        //We don't persist or show confirmPassword

        return userRepository.save(newUser);

    }

}
