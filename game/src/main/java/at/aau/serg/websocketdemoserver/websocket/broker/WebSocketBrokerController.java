package at.aau.serg.websocketdemoserver.websocket.broker;

import at.aau.serg.websocketdemoserver.domains.User;
import at.aau.serg.websocketdemoserver.repository.InMemoryUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class WebSocketBrokerController {

    @Autowired
    private InMemoryUserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MessageMapping("/hello")
    @SendTo("/topic/hello-response")
    public String handleHello(String message) {
        // TODO handle the messages here
        return "echo from broker: " + HtmlUtils.htmlEscape(message);
    }

    /*@MessageMapping("/hello")
    @SendTo("/topic/hello-response")
    public String handleHello(String message) {
        // TODO handle the messages here
        TestMessage testMessage = new TestMessage();
        testMessage.setText("This is a test message");

        //String content = payload.getText();
        String jsonResponse = "{\"messageType\":\"TEST\",\"text\":\"testMessage returned to client\"}";
        return "echo from broker: " + HtmlUtils.htmlEscape(jsonResponse);
    }*/




    @MessageMapping("/saveUser")
    @SendTo("/topic/userSaved")
    public void handleSaveUser(User user) {
        if (!userRepository.isUserExist(user.getUsername())) {
            userRepository.addUser(user);
        }
    }

}
