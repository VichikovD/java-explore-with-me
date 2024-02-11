package ru.yandex.practicum.eventComment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.eventComment.EventCommentRepository;
import ru.yandex.practicum.eventComment.GetEventCommentsRequest;
import ru.yandex.practicum.eventComment.model.EventComment;
import ru.yandex.practicum.eventComment.model.EventCommentMapper;
import ru.yandex.practicum.eventComment.model.dto.EventCommentInfoDto;
import ru.yandex.practicum.eventComment.model.dto.EventCommentRequestDto;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventCommentServiceImpl implements EventCommentService {
    final EventCommentRepository eventCommentRepository;
    final UserRepository userRepository;
    final EventRepository eventRepository;

    @Override
    public EventCommentInfoDto create(long authorId, long eventId, EventCommentRequestDto eventCommentRequestDto) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("User with id=" + authorId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        PublishState eventState = event.getState();
        if (eventState != PublishState.PUBLISHED) {
            throw new DataIntegrityViolationException("Only published events can be commented");
        }

        EventComment commentToSave = EventCommentMapper.requestDtoToModel(eventCommentRequestDto, event, author, false);
        EventComment savedComment = eventCommentRepository.save(commentToSave);

        return EventCommentMapper.modelToInfoDto(savedComment);
    }

    @Override
    public EventCommentInfoDto update(long authorId, long commentId, EventCommentRequestDto eventCommentRequestDto) {
        EventComment eventComment = eventCommentRepository.findByIdAndAuthorId(commentId, authorId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found for authorName with id=" + authorId));

        EventCommentMapper.updateModelByRequestDto(eventComment, eventCommentRequestDto);
        EventComment updatedComment = eventCommentRepository.save(eventComment);

        return EventCommentMapper.modelToInfoDto(updatedComment);
    }

    @Override
    public EventCommentInfoDto update(long commentId, EventCommentRequestDto eventCommentRequestDto) {
        EventComment eventComment = eventCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));

        EventCommentMapper.updateModelByRequestDto(eventComment, eventCommentRequestDto);
        EventComment updatedComment = eventCommentRepository.save(eventComment);

        return EventCommentMapper.modelToInfoDto(updatedComment);
    }

    @Override
    public void delete(long authorId, long commentId) {
        eventCommentRepository.findByIdAndAuthorId(commentId, authorId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found for author with id=" + authorId));
        eventCommentRepository.deleteById(commentId);
    }

    @Override
    public void delete(long commentId) {
        eventCommentRepository.deleteById(commentId);
    }

    @Override
    public List<EventCommentInfoDto> getByAuthorId(long authorId, Pageable pageable) {
        List<EventComment> eventCommentList = eventCommentRepository.findByAuthorId(authorId, pageable);

        return EventCommentMapper.modelListToInfoDtoList(eventCommentList);
    }

    @Override
    public List<EventCommentInfoDto> findByParam(GetEventCommentsRequest getEventCommentsRequest) {
        List<EventComment> eventCommentList = eventCommentRepository.findFilteredAsAdmin(getEventCommentsRequest.getEvents(),
                getEventCommentsRequest.getRangeStart(), getEventCommentsRequest.getRangeEnd(), getEventCommentsRequest.getPageable());

        return EventCommentMapper.modelListToInfoDtoList(eventCommentList);
    }
}
