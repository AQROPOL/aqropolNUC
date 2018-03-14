package hello;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping({"/greeting/{name}", "greeting"})
    public Greeting greeting(@PathVariable(required = false) Optional<String> name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name.orElse("World")));
    }
}