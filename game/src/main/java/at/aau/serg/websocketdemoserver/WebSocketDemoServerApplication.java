package at.aau.serg.websocketdemoserver;

import at.aau.serg.websocketdemoserver.repository.InMemoryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "at.aau.serg.websocketdemoserver")
public class WebSocketDemoServerApplication implements CommandLineRunner {

    @Autowired
    private InMemoryUserRepository userRepository;


    public static void main(String[] args) {
        SpringApplication.run(WebSocketDemoServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

}
