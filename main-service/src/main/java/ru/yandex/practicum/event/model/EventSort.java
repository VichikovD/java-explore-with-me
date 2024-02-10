package ru.yandex.practicum.event.model;

public enum EventSort {
    EVENT_DATE,
    VIEWS;

    public static EventSort from(String sortParam) {
        for (EventSort eventSort : EventSort.values()) {
            if (eventSort.name().equalsIgnoreCase(sortParam)) {
                return eventSort;
            }
        }
        throw new IllegalArgumentException("EventSort: Unknown sort: " + sortParam);
    }
}
