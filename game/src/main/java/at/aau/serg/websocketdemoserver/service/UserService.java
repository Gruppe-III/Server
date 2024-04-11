package at.aau.serg.websocketdemoserver.service;

import at.aau.serg.websocketdemoserver.domains.User;
import at.aau.serg.websocketdemoserver.repository.InMemoryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final InMemoryUserRepository userRepository;

    @Autowired
    public UserService(InMemoryUserRepository userRepository) {

        this.userRepository = userRepository;

        //default data
        User testuser = new User("test@test.at");
        testuser.setPassword("Daniel123");
        System.out.println("test@test.at, password: Daniel123 hinzugefügt");
        System.out.println(testuser.getUsername() + "##" + testuser.getPassword() + "##" + testuser.getId());
        //dieser scheiss hat mich wsl 2h gekostet...
        userRepository.addUser(testuser);
    }


    public User findUserById(String id) {
        return userRepository.findById(id);
    }

    public void addUser(User user) {
        userRepository.addUser(user);
    }

    public void removeUserById(String id) {
        userRepository.removeUser(id);
    }

    public Set<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public User findUserByName(String name) {
        return userRepository.findByName(name);

    }


    public boolean isUserExist(String username) {
        return userRepository.isUserExist(username);
    }

}
