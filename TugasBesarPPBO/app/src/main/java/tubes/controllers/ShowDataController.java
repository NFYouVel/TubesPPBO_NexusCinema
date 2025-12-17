package tubes.controllers;

import java.util.List;

import tubes.models.User;
import tubes.models.exceptions.EmptyListException;
import tubes.repositories.ShowDataUser;

public class ShowDataController {
    private ShowDataUser showDataUserRepository;

    public ShowDataController() {
        showDataUserRepository = new ShowDataUser();
    }

    public List<User> getShowAllUser() throws EmptyListException{
        return showDataUserRepository.getAllUsers();
    }
}
