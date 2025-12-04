package tubes.controllers;

import tubes.models.enums.Roles;
import tubes.models.exception.LoginFailedException;
import tubes.repositories.UserRepository;
import tubes.utils.UtilHashing;
import tubes.models.User;

public class UserController {
    private UserRepository userRepository;

    public UserController() {
        this.userRepository = new UserRepository();
    }

    public Roles loginVerification(String email, String password) throws LoginFailedException {
        User user = this.userRepository.getUser(email);
        
        if (UtilHashing.verifyPassword(password, email, password) == false) {
            throw new LoginFailedException("Wrong Password for email '" + email + "'.");
        }

        if (user.getEmail().equals(email)) {
            String salt = UtilHashing.generateSalt();
            if (UtilHashing.verifyPassword(password, salt, user.getPassword())) {
                return user.getRole();
            }
        }
        
        return null;
    }
}
