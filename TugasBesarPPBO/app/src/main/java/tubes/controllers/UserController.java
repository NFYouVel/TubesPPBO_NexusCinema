package tubes.controllers;

import tubes.repositories.UserRepository;
import tubes.utils.UtilGlobal;
import tubes.utils.UtilHashing;

import tubes.models.User;
import tubes.models.exceptions.LoginFailedException;

public class UserController {
    private UserRepository userRepository;

    public UserController() {
        this.userRepository = new UserRepository();
    }

    public User loginVerification(String email, String password) throws LoginFailedException {
        User user = this.userRepository.getUser(email);
        
        if (UtilHashing.verifyPassword(password, user.getPassword()) == false) {
            throw new LoginFailedException("Wrong Password for email '" + email + "'.");
        }

        if (user.getEmail().equals(email) && UtilHashing.verifyPassword(password, user.getPassword())) {
            UtilGlobal.setGlobalUUID(user.getUserUUID());
            UtilGlobal.setRole(user.getRole());
            return user;
        }
        
        return null;
    }
    public void signupCustomer(User user) {
        user.setPassword(UtilHashing.hashPassword(user.getPassword()));
        this.userRepository.insertUser(user);
    }

    public void signupStaff(User user, String ein, double salary) {
        user.setPassword(UtilHashing.hashPassword(user.getPassword()));
        this.userRepository.insertUser(user, ein, salary);
    }
}
