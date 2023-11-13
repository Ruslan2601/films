package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(int id);

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    List<User> getFriendsList(int id);

    List<User> getCommonFriends(int id, int otherId);
}
