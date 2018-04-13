package hello;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping({"/greeting/{name}", "greeting"})
    public Greeting greeting(@PathVariable(required = false) Optional<String> name) {

        return new Greeting(counter.incrementAndGet(),
                String.format(template, name.orElse("World")));
    }

    @RequestMapping(value = "/greting", method = RequestMethod.POST)
    public ResponseEntity<Greeting> postGreeting(@RequestBody Greeting newGreeting) {
        System.out.println("POST");
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newGreeting.getId()).toUri()).build();
    }
}