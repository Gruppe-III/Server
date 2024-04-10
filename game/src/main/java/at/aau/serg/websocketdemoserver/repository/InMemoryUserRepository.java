package at.aau.serg.websocketdemoserver.repository;

import at.aau.serg.websocketdemoserver.domains.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**hier noch googlen ob component oder repository*/

@Component
//@Repository
public class InMemoryUserRepository {
    private final Set<User> storage = new HashSet<>();

    //public InMemoryUserRepository() {
        // Hier können Sie ggf. Initialisierungen vornehmen

    //}

    /*@Bean
    public InMemoryUserRepository userRepository() {
        return new InMemoryUserRepository();
    }*/

    public User findById(String id) {
        for (User user : storage) {
            if (id.equals(user.getId())) { // Check id against null to avoid NPE
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        //todo

        storage.add(user);

    }

    public void removeUser(String id) {
        //todo
        User userToRemove = null;
        for (User user : storage) {
            if (user.getId().equals(id)) {
                userToRemove = user;
                break;
            }
        }
        if (userToRemove != null) {
            storage.remove(userToRemove);
        }
    }

    public Set<User> findAllUsers() {
        //todo
        return storage;
    }

    public User findByName(String name) {
        for (User user : storage) {
            if (user != null && user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public boolean isUserExist(String username) {
        return findByName(username) != null;
    }
}
