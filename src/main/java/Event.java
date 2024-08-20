package main.java;
import java.awt.event.ActionEvent;

public class Event {

    public enum EventType {
        MOUSE_CLICKED,
        MOUSE_PRESSED,
        MOUSE_RELEASED,
        MOUSE_MOVED,
        MOUSE_DRAGGED,
        KEY_PRESSED,
        KEY_RELEASED,
        KEY_TYPED,
        COLLISION
    }

    private final EventType eventType;
    private boolean consumed = false;

    public Event(EventType eventType) {
        this.eventType =  eventType;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public EventType getEventType() {
        return eventType;
    }

    public boolean isConsumed() {
        return consumed;
    }
}
