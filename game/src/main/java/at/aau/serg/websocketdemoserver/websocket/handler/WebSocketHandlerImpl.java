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

    @Autowired
    private InMemoryUserRepository userRepository;

    @Autowired
    private UserService userService; // hier wurde auf final geändert, laut intelliJ ein vorschlag


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
            case REGISTER:
                handleRegisterAction(session, payload);
                break;
            case DELETE:
                handleDeleteAction(session, userMessage);
                break;
            case UPDATE:
                handleUpdateAction(session, userMessage);
                break;
            case LOGIN:
                handleLoginAction(session, payload);
                break;
            default:
                System.out.println("Unknown action type received");
                break;
        }



        // Send echo back to the client
        //session.sendMessage(new TextMessage("echo from handler: " + payload));
    }



    public void handleDeleteAction (WebSocketSession session, UserMessage userMessage) {

    }

    public void handleUpdateAction (WebSocketSession session, UserMessage userMessage) {

    }


    public void handleLoginAction(WebSocketSession session, String payload) throws Exception {
        System.out.println("reached handle login action beginning");
        UserMessage userMessage = gson.fromJson(payload, UserMessage.class);
        User userToCheck = new User(userMessage.getName(), userMessage.getPassword());
        System.out.println(userToCheck.getUsername() + "//" + userToCheck.getPassword() );


        User userFound = userService.findUserByName(userToCheck.getUsername());
        if (userFound != null) {
            System.out.println("juhu - found");
        }

        System.out.println("reached handle at if cause");
        if (userFound != null) {
            if (userFound.getPassword().equals(userToCheck.getPassword())) {
                System.out.println("user found");

                userMessage.setActionType(UserMessage.ActionType.LOGIN_OK);
                userMessage.setId(userFound.getId());
                userMessage.setName(userFound.getUsername());
                userMessage.setPassword(userFound.getPassword());
                String payloadExport = gson.toJson(userMessage);
                session.sendMessage(new TextMessage(payloadExport));
            }
            else {
                System.out.println("user found, but password not ok");
                userMessage.setActionType(UserMessage.ActionType.LOGIN_ERR);
                userMessage.setId("login_err");
                userMessage.setName("login_err");
                userMessage.setPassword("login_err");
                String payloadExport = gson.toJson(userMessage);
                session.sendMessage(new TextMessage(payloadExport));
            }

        }
        else {
            System.out.println("user not found");
            userMessage.setActionType(UserMessage.ActionType.LOGIN_ERR);
            userMessage.setId("login_err");
            userMessage.setName("login_err");
            userMessage.setPassword("login_err");
            String payloadExport = gson.toJson(userMessage);
            session.sendMessage(new TextMessage(payloadExport));
        }

    }

    public void handleRegisterAction (WebSocketSession session, String payload) throws Exception{
        System.out.println("reached handle register action beginning");
        UserMessage userMessage = gson.fromJson(payload, UserMessage.class);
        User userToCheck = new User(userMessage.getName(), userMessage.getPassword());
        System.out.println(userMessage.getName() + "//" + userMessage.getPassword() );

        //User userFound = userService.findUserByName(userToCheck.getUsername());
        System.out.println("user found -->" + userToCheck.getId());

        if (userToCheck.getId() != null) {
            //
            //if null, dann user zu db
            //
            System.out.println("user already exists in db");
            userMessage.setActionType(UserMessage.ActionType.REGISTER_ERR);
            userMessage.setId("err");
            userMessage.setName("err");
            userMessage.setPassword("err");
            //
            String payloadExport = gson.toJson(userMessage);
            session.sendMessage(new TextMessage(payloadExport));

        }
        else {
            //
            System.out.println("no user found in db --> add");
            User userToRegister = new User(userMessage.getName());
            userToRegister.setPassword(userMessage.getPassword());
            userService.addUser(userToRegister);

            userMessage.setActionType(UserMessage.ActionType.REGISTER_OK);
            userMessage.setId(userToRegister.getId());
            userMessage.setName(userToRegister.getUsername());
            userMessage.setPassword(userToRegister.getPassword());
            //
            String payloadExport = gson.toJson(userMessage);
            session.sendMessage(new TextMessage(payloadExport));

        }

        //String payloadExport = gson.toJson(userMessage);
        //session.sendMessage(new TextMessage(payloadExport));

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
