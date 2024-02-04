package ru.yandex.practicum.eventRequest.model;

public enum EventRequestStatus {
    PENDING,
    CONFIRMED,
    CANCELED,
    REJECTED;

    public static EventRequestStatus from(String status) {
        for (EventRequestStatus requestStatus : EventRequestStatus.values()) {
            if (requestStatus.name().equalsIgnoreCase(status)) {
                return requestStatus;
            }
        }
        throw new IllegalArgumentException("EventRequestStatus: Unknown status: " + status);
    }
}
