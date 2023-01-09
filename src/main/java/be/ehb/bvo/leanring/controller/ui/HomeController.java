package be.ehb.bvo.leanring.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping(value="/")
    @RequestMapping(value="/",method= RequestMethod.GET)
    public String hello(Principal principal){
        if(principal != null) {
            System.out.println(principal.getName());
        } else {
            System.out.println("Not logged in...");
        }

        return "home";
    }
}