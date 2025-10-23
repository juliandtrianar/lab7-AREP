package edu.eci.arep.lab8.service;

import java.util.ArrayList;
import java.util.List;
import edu.eci.arep.lab8.model.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {
     private static List<User> users = new ArrayList<>();

    public List<User> getAllUsers(){
        return users;
    }

    public User saveUser(User user){
        users.add(user);
        return user;
    }
    
}
