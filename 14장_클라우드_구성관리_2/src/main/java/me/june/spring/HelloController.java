package me.june.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final GreetingProps greetingProps;

    public HelloController(GreetingProps greetingProps) {
        this.greetingProps = greetingProps;
    }

    @GetMapping("/hello")
    public String hello() {
        return greetingProps.getMessage();
    }
}
