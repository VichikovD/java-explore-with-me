package ru.yandex.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.comment.CommentDeleteParam;
import ru.yandex.practicum.comment.CommentGetParam;
import ru.yandex.practicum.comment.CommentUpdateParam;
import ru.yandex.practicum.comment.EventCommentRepository;
import ru.yandex.practicum.comment.model.EventComment;
import ru.yandex.practicum.comment.model.EventCommentMapper;
import ru.yandex.practicum.comment.model.dto.EventCommentInfoDto;
import ru.yandex.practicum.comment.model.dto.EventCommentRequestDto;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.PublishState;
import ru.yandex.practicum.event.repository.EventRepository;
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
    public EventCommentInfoDto update(CommentUpdateParam updateParam) {
        long commentId = updateParam.getCommentId();
        Long authorId = updateParam.getAuthorId();
        EventComment eventComment;

        if (authorId != null) {
            eventComment = eventCommentRepository.findByIdAndAuthorId(commentId, authorId)
                    .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found for authorName with id=" + authorId));
        } else {
            eventComment = eventCommentRepository.findById(commentId)
                    .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
        }

        EventCommentMapper.updateModelByRequestDto(eventComment, updateParam.getEventCommentRequestDto());
        EventComment updatedComment = eventCommentRepository.save(eventComment);

        return EventCommentMapper.modelToInfoDto(updatedComment);
    }

    @Override
    public void delete(CommentDeleteParam deleteParam) {
        long commentId = deleteParam.getCommentId();
        Long authorId = deleteParam.getAuthorId();

        if (authorId != null) {
            eventCommentRepository.findByIdAndAuthorId(commentId, authorId)
                    .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found for author with id=" + authorId));
        }
        eventCommentRepository.deleteById(commentId);
    }

    @Override
    public List<EventCommentInfoDto> getByAuthorId(long authorId, Pageable pageable) {
        List<EventComment> eventCommentList = eventCommentRepository.findByAuthorId(authorId, pageable);

        return EventCommentMapper.modelListToInfoDtoList(eventCommentList);
    }

    @Override
    public List<EventCommentInfoDto> findByParam(CommentGetParam commentGetParam) {
        List<EventComment> eventCommentList = eventCommentRepository.findByParam(commentGetParam.getEvents(),
                commentGetParam.getRangeStart(), commentGetParam.getRangeEnd(), commentGetParam.getPageable());

        return EventCommentMapper.modelListToInfoDtoList(eventCommentList);
    }
}
