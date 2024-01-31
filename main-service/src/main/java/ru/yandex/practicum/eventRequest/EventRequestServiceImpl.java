package ru.yandex.practicum.eventRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.eventRequest.model.EventRequest;
import ru.yandex.practicum.eventRequest.model.EventRequestInfoDto;
import ru.yandex.practicum.eventRequest.model.EventRequestMapper;
import ru.yandex.practicum.eventRequest.model.EventRequestStatus;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventRequestServiceImpl implements EventRequestService {
    final EventRequestRepository eventRequestRepository;
    final UserRepository userRepository;
    final EventRepository eventRepository;

    @Override
    public EventRequestInfoDto create(long requesterId, long eventId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("User with id=" + requesterId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getInitiator().getId().equals(requesterId)) {
            throw new DataIntegrityViolationException("Initiator can't create request for his own event");
        }
        if (!PublishState.PUBLISHED.equals(event.getState())) {
            throw new DataIntegrityViolationException("Event not yet published");
        }
        long confirmedRequests = eventRequestRepository.countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED);
        long participantLimit = event.getParticipantLimit();
        if (confirmedRequests >= participantLimit) {
            throw new DataIntegrityViolationException("Participant limit (" + participantLimit + ") is reached");
        }

        EventRequest eventRequestToSave = EventRequest.builder()
                .event(event)
                .requester(requester)
                .created(LocalDateTime.now())
                .build();
        if (event.isRequestModeration()) {
            eventRequestToSave.setStatus(EventRequestStatus.PENDING);
        } else {
            eventRequestToSave.setStatus(EventRequestStatus.CONFIRMED);
        }
        EventRequest eventRequestSaved = eventRequestRepository.save(eventRequestToSave);
        return EventRequestMapper.toInfoDto(eventRequestSaved);
    }

    @Override
    public List<EventRequestInfoDto> getAllByRequesterId(long requesterId) {
        userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("User with id=" + requesterId + " was not found"));
        List<EventRequest> eventRequestList = eventRequestRepository.findAllByRequesterId(requesterId);
        return EventRequestMapper.listModelToListInfoDto(eventRequestList);
    }
}
