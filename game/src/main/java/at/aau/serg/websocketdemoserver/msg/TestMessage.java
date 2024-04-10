package at.aau.serg.websocketdemoserver.msg;

public class TestMessage {
    private final MessageType messageType = MessageType.TEST;

    private String text;

    public TestMessage() {
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public TestMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
