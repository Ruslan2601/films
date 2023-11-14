package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.UserValidation;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Transactional
    public User addUser(User user, BindingResult bindingResult) {
        UserValidation.validation(user, bindingResult);
        return userStorage.addUser(user);
    }

    @Transactional
    public User updateUser(User user, BindingResult bindingResult) {
        UserValidation.validation(user, bindingResult);
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    @Transactional
    public void addFriend(int id, int friendId) {
        if (id < 0 || friendId < 0) {
            throw new ObjectNotFoundException("Id не может быть меньше 0");
        }
        userStorage.addFriend(id, friendId);
    }

    @Transactional
    public void removeFriend(int id, int friendId) {
        userStorage.removeFriend(id, friendId);
    }


    public List<User> getFriendsList(int id) {
        return userStorage.getFriendsList(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}
