package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        return userService.addUser(user, bindingResult);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        return userService.updateUser(user, bindingResult);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id,
                          @PathVariable("friendId") Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Integer id,
                             @PathVariable("friendId") Integer friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@PathVariable("id") Integer id) {
        return userService.getFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id,
                                     @PathVariable("otherId") Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUserByID(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }
}
