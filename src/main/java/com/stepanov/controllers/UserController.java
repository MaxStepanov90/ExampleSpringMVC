package com.stepanov.controllers;

import com.stepanov.dao.UsersDao;
import com.stepanov.forms.UserForm;
import com.stepanov.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UsersDao usersDao;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView getAllUsers(@RequestParam(value = "first_name", required = false) String firstName) {
        List<User> users = null;
        if (firstName != null) {
            users = usersDao.findAllByFirstName(firstName);
        } else {
            users = usersDao.findAll();
        }
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("usersFromServer", users);
        return modelAndView;
    }
    @RequestMapping(path = "/users/{user-id}",method = RequestMethod.GET)
    public ModelAndView getUserById(@PathVariable("user-id") Long userId){
        Optional<User> userCandidate = usersDao.find(userId);
        ModelAndView modelAndView = new ModelAndView("users");
        userCandidate.ifPresent(user -> modelAndView.addObject("usersFromServer", Collections.singletonList(user)));
        return modelAndView;
    }
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String addUser(UserForm userForm){
        User newUser = User.from(userForm);
        usersDao.save(newUser);
        return "redirect:/users";
    }
}
