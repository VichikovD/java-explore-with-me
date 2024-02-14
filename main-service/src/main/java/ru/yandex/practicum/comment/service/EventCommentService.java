package ru.yandex.practicum.comment.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.comment.CommentDeleteParam;
import ru.yandex.practicum.comment.CommentGetParam;
import ru.yandex.practicum.comment.CommentUpdateParam;
import ru.yandex.practicum.comment.model.dto.EventCommentInfoDto;
import ru.yandex.practicum.comment.model.dto.EventCommentRequestDto;

import java.util.List;

public interface EventCommentService {
    EventCommentInfoDto create(long authorId, long eventId, EventCommentRequestDto eventCommentRequestDto);

    EventCommentInfoDto update(CommentUpdateParam updateParam);

    void delete(CommentDeleteParam deleteParam);

    List<EventCommentInfoDto> getByAuthorId(long authorId, Pageable pageable);

    List<EventCommentInfoDto> findByParam(CommentGetParam commentGetParam);
}
