package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidation;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    protected final Map<Integer, User> userMap = new HashMap<>();
    protected final Set<Friends> friends = new HashSet<>();
    private int id = 0;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User addUser(User user, BindingResult bindingResult) {
        UserValidation.validation(user, bindingResult);
        user.setId(++id);
        userMap.put(user.getId(), user);
        log.info("Добавлен пользователь");
        return user;
    }

    @Override
    public User updateUser(User user, BindingResult bindingResult) {
        UserValidation.validation(user, bindingResult);
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("Обновлены данные по пользователю");
        } else {
            throw new ObjectNotFoundException("Пользователя с такими id нет");
        }
        return user;
    }

    @Override
    public User getUserById(int id) {
        if (userMap.containsKey(id)) {
            log.info("Пользователь с id = '{}' найден", id);
            return userMap.get(id);
        } else {
            throw new ObjectNotFoundException("Пользователя с такими id нет");
        }
    }

    @Override
    public void addFriend(int id, int friendId) {
        if (id < 0 || friendId < 0) {
            throw new ObjectNotFoundException("Id не может быть меньше 0");
        }
        getUserById(id);
        getUserById(friendId);
        Friends newFriends = new Friends(id, friendId);
        friends.add(newFriends);
        log.info("Был успешно добавлен друг с id = '{}' для пользователя с id = '{}'", friendId, id);
    }

    @Override
    public void removeFriend(int id, int friendId) {
        getUserById(id);
        getUserById(friendId);
        Friends newFriends = new Friends(id, friendId);
        friends.removeIf(friends -> friends.equals(newFriends));
        log.info("Был успешно удален друг с id = '{}' для пользователя с id = '{}'", friendId, id);
    }

    @Override
    public List<User> getFriendsList(int id) {
        return friends.stream().filter(friends -> friends.getId() == id)
                .map(friend -> getUserById(friend.getFriend_id()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        List<User> userList = new ArrayList<>(getFriendsList(id));
        userList.retainAll(getFriendsList(otherId));
        return userList;
    }

}
