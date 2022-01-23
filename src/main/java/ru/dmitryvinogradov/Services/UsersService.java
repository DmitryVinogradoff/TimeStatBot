package ru.dmitryvinogradov.Services;

import ru.dmitryvinogradov.DAO.UserDao;
import ru.dmitryvinogradov.Models.Users;

import java.util.List;

public class UsersService {
    private UserDao usersDao = new UserDao();


    public UsersService() {}

    public Users findUser(int id){
        return usersDao.findById(id);
    }

    public void saveUser(Users users){
        usersDao.save(users);
    }

    public void deleteUser(Users users){
        usersDao.delete(users);
    }

    public void updateUser(Users users){
        usersDao.update(users);
    }

    public List<Users> findAllUsers(){
        return usersDao.findAll();
    }
}
