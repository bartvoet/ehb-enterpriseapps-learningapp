package be.ehb.bvo.leanring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class HomeController {

    //@GetMapping(value="/")
    //@RequestMapping(value="/",method= RequestMethod.GET)
    public String hello(){
        return "Hello World!!";
    }
}