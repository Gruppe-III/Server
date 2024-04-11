package at.aau.serg.websocketdemoserver.msg;

public class UserMessage {

    private final MessageType messageType = MessageType.USER;

    private String name;
    private String password;
    private ActionType actionType;
    private String id;

    private Double points;

    public UserMessage(String name, String password) {

        this.name = name;
        this.password = password;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public enum ActionType {
        OK, ADD, DELETE, UPDATE, ERROR, DUPLICATE, REGISTER, REGISTER_OK, REGISTER_ERR, LOGIN, LOGIN_OK, LOGIN_ERR
    }
}


