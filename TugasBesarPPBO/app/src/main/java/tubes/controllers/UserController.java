package tubes.controllers;

import tubes.repositories.UserRepository;
import tubes.utils.UtilGlobal;
import tubes.utils.UtilHashing;

import tubes.models.User;
import tubes.models.enums.Roles;
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

    public void signupUser(User user) {
        user.setPassword(UtilHashing.hashPassword(user.getPassword()));
        if (user.getRole() != Roles.CUSTOMER) {
            double salary = 0;
            if (user.getRole() == Roles.STAFF) {
                salary = 4500000;
            } else if (user.getRole() == Roles.MANAGER) {
                salary = 6000000;
            }
            this.userRepository.insertUser(user, salary);
        }else{
            this.userRepository.insertUser(user);
        }
    }
}
