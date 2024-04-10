package at.aau.serg.websocketdemoserver.msg;

public class UserMessage {

    private final MessageType messageType = MessageType.USER;

    private String name;
    private ActionType actionType;
    private String id;

    private Double points;

    public UserMessage(String name) {
        this.name = name;
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
        ADD, DELETE, UPDATE, ERROR, DUPLICATE
    }
}


