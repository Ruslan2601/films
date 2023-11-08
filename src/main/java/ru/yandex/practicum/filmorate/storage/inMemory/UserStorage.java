package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsers();

    User addUser(User user, BindingResult bindingResult);

    User updateUser(User user, BindingResult bindingResult);

    User getUserById(int id);

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    List<User> getFriendsList(int id);

    List<User> getCommonFriends(int id, int otherId);
}
