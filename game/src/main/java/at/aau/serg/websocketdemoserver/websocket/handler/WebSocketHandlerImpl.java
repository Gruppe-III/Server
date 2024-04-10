package at.aau.serg.websocketdemoserver.websocket.handler;

import at.aau.serg.websocketdemoserver.msg.MessageType;
import at.aau.serg.websocketdemoserver.repository.InMemoryUserRepository;
import at.aau.serg.websocketdemoserver.domains.User;
import at.aau.serg.websocketdemoserver.msg.BaseMessage;
import at.aau.serg.websocketdemoserver.msg.TestMessage;
import at.aau.serg.websocketdemoserver.msg.UserMessage;
import at.aau.serg.websocketdemoserver.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

@Component
@Service
public class WebSocketHandlerImpl implements WebSocketHandler {

    final private Gson gson = new Gson();

    /*@Autowired
    private InMemoryUserRepository userRepository;*/

    @Autowired
    private UserService userService;


    public WebSocketHandlerImpl() {
        this.userService = new UserService(new InMemoryUserRepository());
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("reached point of afterConnectionEstablished");



    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("reached point of handleMessage");

        // TODO handle the messages here
        //session.sendMessage(new TextMessage("echo from handler: " + message.getPayload()));
        String payload = (String) message.getPayload();
        System.out.println("from client" + payload);

        handleMessageByType(session, payload);
    }

    private void handleMessageByType(WebSocketSession session, String payload) throws Exception {
        // Convert JSON to a generic message object to determine its type
        BaseMessage messageBase = gson.fromJson(payload, BaseMessage.class);

        // Check the message type and handle accordingly
        if (messageBase != null) {
            MessageType messageType = messageBase.getMessageType();
            switch (messageType) {
                case TEST -> handleTestMessage(session, payload);
                case USER -> handleUserMessage(session, payload);
                default -> System.out.println("Unknown message type received");
            }
        }
    }

    private void handleTestMessage(WebSocketSession session, String payload) throws Exception {
        //1 user message aus der payload extrahieren
        TestMessage testMessage = gson.fromJson(payload, TestMessage.class);
        //2 add additional information to message with set
        testMessage.setText("juhu");
        //make a new payload
        String payloadExport = gson.toJson(testMessage);
        //printTo terminal
        System.out.println("Received TestMessage: " + testMessage.getText());
        // Send echo back to the client --> export
        //umbedingt payload verwenden!!!!!!
        //session.sendMessage(new TextMessage("echo from handler: " + payload));
        session.sendMessage(new TextMessage(payloadExport));

        //payload --> UserMessage --> userMessage.set... --> Sting payloadExport = gson.toJson(userMessage); -->
        //session.sendMessage(new TextMessage(payloadExport));

    }

    private void handleUserMessage(WebSocketSession session, String payload) throws Exception {
        //1
        UserMessage userMessage = gson.fromJson(payload, UserMessage.class);

        System.out.println("Received TestMessage: ");
        System.out.println("Received TestMessage: " + userMessage);

        // Convert JSON to UserMessage object
        //UserMessage userMessage = gson.fromJson(payload, UserMessage.class);

        // Process the UserMessage based on its action type
        switch (userMessage.getActionType()) {
            case ADD:
                handleAddAction(session, payload);
                break;
            case DELETE:
                handleDeleteAction(session, userMessage);
                break;
            case UPDATE:
                handleUpdateAction(session, userMessage);
                break;
            default:
                System.out.println("Unknown action type received");
                break;
        }



        // Send echo back to the client
        //session.sendMessage(new TextMessage("echo from handler: " + payload));
    }

    public void handleAddAction (WebSocketSession session, String payload) throws Exception{
        //session.sendMessage(new TextMessage(payload));
        //1 user message aus der payload extrahieren
        UserMessage userMessage = gson.fromJson(payload, UserMessage.class);
        System.out.println("##" + payload + "##");
        //2 user aus usermessage erstellen
        User userToAdd = new User(userMessage.getName());
        System.out.println("##");
        System.out.println(userToAdd.getId());
        System.out.println(userToAdd.getUsername());
        System.out.println(userToAdd.getPoints());
        System.out.println("##");

        //wenn jemand gefunden wurde
        if(userService.findUserByName(userMessage.getName()) != null) {
            System.out.println("duplicate");
            userMessage.setActionType(UserMessage.ActionType.DUPLICATE);
        }
        else {
            System.out.println("no duplicate");
            userService.addUser(userToAdd);
            userMessage.setId(userToAdd.getId());
            userMessage.setName(userMessage.getName());
        }
        String payloadExport = gson.toJson(userMessage);
        session.sendMessage(new TextMessage(payloadExport));
        System.out.println("send payload to client");


        //wenn er nicht gefunden wird, kann man ihn hinzufügen
        /*if (userRepository.findById(userToAdd.getId()) == null) {
            userRepository.addUser(userToAdd);
            System.out.println("user added");
        }*/
        //userToAdd.setPoints(0.0);






        //if findByName != null --> user found --> duplicate
        /*if (userRepository.findByName(userToAdd.getUsername()) != null)  {
            userMessage.setActionType(UserMessage.ActionType.DUPLICATE);
            String payloadExport = gson.toJson();
            System.out.println("duplicate");

        }
        else {
            System.out.println("no duplicate");
        }*/

        /**
         * vll vom Objekt zur Message --> zur payload*/

        //String payloadExport = gson.toJson(userMessage);
        //session.sendMessage(new TextMessage(payloadExport));
        //3 wenn user existiert, dann... duplicate




    }

    public void handleDeleteAction (WebSocketSession session, UserMessage userMessage) {

    }

    public void handleUpdateAction (WebSocketSession session, UserMessage userMessage) {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("WebSocket transport error: " + exception.getMessage());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("WebSocket connection closed: " + closeStatus.getCode() + ", " + closeStatus.getReason());


    }

    @Override
    public boolean supportsPartialMessages() {
        System.out.println("reached point of supportsPartialMessages");

        return false;
    }
}
