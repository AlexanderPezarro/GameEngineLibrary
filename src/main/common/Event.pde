public static class Event {
    enum EventType {
        CLICK,
        ATTACK
    };

    boolean consumed;
    final EventType type;

    public Event(EventType type) {
        this.type = type;
        consumed = false;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public boolean isConsumed() {
        return consumed;
    }
}