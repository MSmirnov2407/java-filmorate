package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.AbstractService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends Controller<User> {

    private final AbstractService<User> userService;

    @Autowired
    public UserController(AbstractService<User> userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен список пользователей");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        User user = userService.getById(id);
        log.info("Получен пользователь по id={}", user.getId());
        return user;
    }

    @PostMapping
    public User postNewUser(@Valid @RequestBody User newUser) {
        User user = userService.create(newUser);
        log.info("Создан Пользователь. Id = {}, email = {}",user.getId(), user.getEmail());
        return user;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User updatedUser) {
        User user= userService.update(updatedUser);
        log.info("Обновлен Пользователь. Id = {}, email = {}",user.getId(), user.getEmail());
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void setFriendship(@PathVariable int id, @PathVariable int friendId) {
        ((UserService) userService).addFriend(id, friendId);
        log.info("Пользователю с id={} добавлен в друзья пользователь с id={}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriendship(@PathVariable int id, @PathVariable int friendId) {
        ((UserService) userService).removeFriend(id, friendId);
        log.info("У пользователя с id={} удален из друзей пользователь с id={}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("У пользователя с id={} запрошен список друзей", id);
        return ((UserService) userService).getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends (@PathVariable int id, @PathVariable int otherId){
        log.info("У пользователей с id={}  и {} запрошен список общих друзей", id, otherId);
        return  ((UserService) userService).getCommonFriends(id,otherId);
    }
}