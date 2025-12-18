package tubes.controllers;

import java.util.List;

import tubes.models.User;
import tubes.models.exceptions.EmptyListException;
import tubes.repositories.ManagerRepository;

public class ManagerController {
    private ManagerRepository managerRepository;

    public ManagerController() {
        this.managerRepository = new ManagerRepository();
    }

    public List<User> getAllUsers() throws EmptyListException {
        return managerRepository.getAllUsers();
    }
}
