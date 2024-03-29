package ru.yandex.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.comment.CommentDeleteParam;
import ru.yandex.practicum.comment.CommentUpdateParam;
import ru.yandex.practicum.comment.model.dto.EventCommentInfoDto;
import ru.yandex.practicum.comment.model.dto.EventCommentRequestDto;
import ru.yandex.practicum.comment.service.EventCommentService;
import ru.yandex.practicum.util.OffsetPageable;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Validated
public class EventCommentPrivateController {
    final EventCommentService eventCommentService;

    @PostMapping("/comments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventCommentInfoDto create(@PathVariable(name = "userId") long authorId,
                                      @RequestParam(name = "eventId") long eventId,
                                      @RequestBody @Validated EventCommentRequestDto eventCommentRequestDto) {
        log.info("POST \"/user/{}/events/{}/comments\" Body={}", authorId, eventId, eventCommentRequestDto);
        EventCommentInfoDto eventRequestList = eventCommentService.create(authorId, eventId, eventCommentRequestDto);
        log.debug("EventRequestList created=" + eventRequestList);
        return eventRequestList;
    }

    @PatchMapping("/comments/{commentId}")
    public EventCommentInfoDto update(@PathVariable(name = "userId") long authorId,
                                      @PathVariable(name = "commentId") long commentId,
                                      @RequestBody @Validated EventCommentRequestDto eventCommentRequestDto) {
        log.info("PATCH \"/user/{}/comments{}\" Body={}", authorId, commentId, eventCommentRequestDto);
        CommentUpdateParam commentUpdateParam = CommentUpdateParam.builder()
                .commentId(commentId)
                .authorId(authorId)
                .eventCommentRequestDto(eventCommentRequestDto)
                .build();

        EventCommentInfoDto eventRequestList = eventCommentService.update(commentUpdateParam);
        log.debug("EventRequest= updated=" + eventRequestList);
        return eventRequestList;
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByAuthorIdAndCommentId(@PathVariable(name = "userId") long authorId,
                                             @PathVariable(name = "commentId") long commentId) {
        log.info("DELETE \"/user/{}/events/{}/comments\"", authorId, commentId);
        CommentDeleteParam deleteParam = CommentDeleteParam.builder()
                .authorId(authorId)
                .commentId(commentId)
                .build();

        eventCommentService.delete(deleteParam);
        log.debug("EventRequestList deleted with id=" + commentId);
    }

    @GetMapping("/comments")
    public List<EventCommentInfoDto> getByAuthorId(@PathVariable(name = "userId") long authorId,
                                                   @RequestParam(name = "from", defaultValue = "0") int offset,
                                                   @RequestParam(name = "size", defaultValue = "10") int limit) {
        log.info("GET \"/user/{}/comments?&from={}&size={}\"", authorId, offset, limit);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdOn");
        Pageable pageable = new OffsetPageable(offset, limit, sort);
        List<EventCommentInfoDto> eventRequestList = eventCommentService.getByAuthorId(authorId, pageable);
        log.debug("EventRequestList found=" + eventRequestList);
        return eventRequestList;
    }
}
