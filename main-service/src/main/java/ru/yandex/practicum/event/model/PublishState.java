package ru.yandex.practicum.event.model;

public enum PublishState {
    PUBLISHED,
    REJECTED,
    WAITING;
    //CANCELED;

    public static PublishState from(String stateParam) {
        for (PublishState publishState : PublishState.values()) {
            if (publishState.name().equalsIgnoreCase(stateParam)) {
                return publishState;
            }
        }
        throw new IllegalArgumentException("PublishState: Unknown state: " + stateParam);
    }

    public enum StateAction {
        PUBLISH_EVENT,
        REJECT_EVENT;
        //CANCEL_EVENT;

        public StateAction from(String stateParam) {
            for (StateAction stateAction : StateAction.values()) {
                if (stateAction.name().equalsIgnoreCase(stateParam)) {
                    return stateAction;
                }
            }
            throw new IllegalArgumentException("StateAction: Unknown state: " + stateParam);
        }

        public static PublishState stateFromAction(StateAction action) {
            if (PUBLISH_EVENT.equals(action)) {
                return PUBLISHED;
            } else if (REJECT_EVENT.equals(action)) {
                return REJECTED;
            /*} else if (CANCEL_EVENT.equals(action)) {
                return CANCELED;*/
            } else {
                throw new IllegalArgumentException("StateAction: Unknown state: " + action);
            }
        }
    }
}
