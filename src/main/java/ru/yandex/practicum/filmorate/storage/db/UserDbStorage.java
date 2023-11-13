package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FriendsMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUsers() {
        List<User> users = jdbcTemplate.query(
                "SELECT user_id, login, name, birthday, email FROM users", new UserMapper());
        log.info("Отображаем список пользователей");
        return users;
    }

    @Override
    public User addUser(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (login, name, birthday, email) VALUES (?, ?, ?, ?)",
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getEmail());
        User thisUser = jdbcTemplate.queryForObject(
                "SELECT user_id, login, name, birthday, email FROM users WHERE login=? "
                        + "AND name=? AND birthday=? AND email=?",
                new UserMapper(), user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getEmail());
        log.info("Добавлен пользователь {}", thisUser);
        return thisUser;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(
                "UPDATE users SET login=?, name=?, birthday=?, email=? WHERE user_id=?",
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getEmail(),
                user.getId());
        User thisUser = getUserById(user.getId());
        log.info("Обновлены данные пользователя {}", thisUser);
        return thisUser;
    }

    @Override
    public User getUserById(int id) {
        try {
            User thisUser = jdbcTemplate.queryForObject(
                    "SELECT user_id, login, name, birthday, email FROM users WHERE user_id=?",
                    new UserMapper(), id);
            log.info("Пользователь с id = '{}' найден", id);
            return thisUser;
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Пользователя с таким id нет");
        }

    }

    @Override
    public void addFriend(int id, int friendId) {
        try {
            jdbcTemplate.queryForObject(
                    "SELECT user_id, friend_id FROM friends WHERE user_id=? AND friend_id=?",
                    new FriendsMapper(), id, friendId);
            log.info("Данные пользователь уже в списке ваших друзей");
        } catch (EmptyResultDataAccessException e) {
            jdbcTemplate.update(
                    "INSERT INTO friends (user_id, friend_id) values (?,?)",
                    id, friendId);
            log.info("Был успешно добавлен друг с id = '{}' для пользователя с id = '{}'", friendId, id);
        }


    }

    @Override
    public void removeFriend(int id, int friendId) {
        jdbcTemplate.update(
                "DELETE from friends  WHERE user_id=? AND friend_id=?",
                id, friendId);
        log.info("Был успешно удален друг с id = '{}' для пользователя с id = '{}'", friendId, id);
    }

    @Override
    public List<User> getFriendsList(int id) {
        List<Integer> usersID = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT friend_id FROM friends WHERE user_id=?",
                id);
        while (userRows.next()) {
            Integer userID = userRows.getInt("friend_id");
            usersID.add(userID);
        }
        log.info("Отображен список ваших друзей");
        return usersID.stream().map(this::getUserById).collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        List<User> userList = new ArrayList<>(getFriendsList(id));
        userList.retainAll(getFriendsList(otherId));
        log.info("Отображен список общих друзей");
        return userList;
    }
}
