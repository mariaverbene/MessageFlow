package kafkaspring.controller;

import kafkaspring.beans.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControllerClass {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/messages")
    public Object count() {
        return userService.getMessages();
    }

}



