package ru.yandex.practicum.event.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.StatisticClient;
import ru.yandex.practicum.category.CategoryRepository;
import ru.yandex.practicum.comment.EventCommentRepository;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.event.repository.LocationRepository;
import ru.yandex.practicum.event.service.EventService;
import ru.yandex.practicum.eventRequest.EventRequestRepository;
import ru.yandex.practicum.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    @Mock
    EventRepository eventRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    LocationRepository locationRepository;
    @Mock
    EventRequestRepository eventRequestRepository;
    @Mock
    StatisticClient statisticClient;
    @Mock
    EventCommentRepository eventCommentRepository;

    @InjectMocks
    EventServiceImpl eventService;

    @Test
    void updateWithoutValidation() {
    }

    @Test
    void findFullDtosFiltered() {
    }

    @Test
    void findShortDtosFiltered() {
    }

    @Test
    void getPublishedById() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void getByInitiatorIdFiltered() {
    }

    @Test
    void getByIdAndInitiatorId() {
    }

    @Test
    void getEventRequestsByInitiatorIdAndEventId() {
    }

    @Test
    void updateEventRequests() {
    }

    private Location getLocation() {
        return new Location(1L, 55.162700f, 61.390888f);
    }

    private Location getLocationWithoutId() {
        return new Location(null, 55.162700f, 61.390888f);
    }
}