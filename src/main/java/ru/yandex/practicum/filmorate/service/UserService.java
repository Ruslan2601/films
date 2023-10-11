package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalServiceException ;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        if (id < 0 || friendId < 0) {
            throw new ObjectNotFoundException("Id не может быть меньше 0");
        }
        User user = userStorage.getUserById(id);
        User userFriend = userStorage.getUserById(friendId);
        user.addFriend(friendId);
        userFriend.addFriend(id);
        log.info("Был успешно добавлен друг с id = '{}' для пользователя с id = '{}'", friendId, id);
    }

    public void removeFriend(int id, int friendId) {
        User user = userStorage.getUserById(id);
        User userFriend = userStorage.getUserById(friendId);
        user.deleteFriend(friendId);
        userFriend.deleteFriend(id);
        log.info("Был успешно удален друг с id = '{}' для пользователя с id = '{}'", friendId, id);
    }

    public List<User> getFriendsList(int id) {
        User user = userStorage.getUserById(id);
        if (user.getFriends().isEmpty()) {
            return new ArrayList<>();
        }
        return user.getFriends().stream().map(userStorage::getUserById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> userList = new ArrayList<>(getFriendsList(id));
        userList.retainAll(getFriendsList(otherId));
        return userList;
    }
}
