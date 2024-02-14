package ru.yandex.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.comment.CommentDeleteParam;
import ru.yandex.practicum.comment.CommentGetParam;
import ru.yandex.practicum.comment.CommentUpdateParam;
import ru.yandex.practicum.comment.model.dto.EventCommentInfoDto;
import ru.yandex.practicum.comment.model.dto.EventCommentRequestDto;
import ru.yandex.practicum.comment.service.EventCommentService;
import ru.yandex.practicum.util.OffsetPageable;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Validated
public class EventCommentAdminController {
    final EventCommentService eventCommentService;

    @GetMapping
    public List<EventCommentInfoDto> getFiltered(@RequestParam(name = "events", required = false) List<Long> events,
                                                 @RequestParam(name = "rangeStart", required = false)
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                 @RequestParam(name = "rangeEnd", required = false)
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                 @RequestParam(name = "from", defaultValue = "0") int offset,
                                                 @RequestParam(name = "size", defaultValue = "10") int limit) {
        log.info("GET \"/admin/comments?events={}&rangeStart={}&rangeEnd={}&from={}&size={}\"", events, rangeStart,
                rangeEnd, offset, limit);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdOn");
        Pageable pageable = new OffsetPageable(offset, limit, sort);

        CommentGetParam commentParam = CommentGetParam.builder()
                .events(events)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .pageable(pageable)
                .build();

        List<EventCommentInfoDto> eventCommentInfoDtoList = eventCommentService.findByParam(commentParam);

        log.debug("EventComments found=" + eventCommentInfoDtoList);
        return eventCommentInfoDtoList;
    }

    @PatchMapping("/{commentId}")
    public EventCommentInfoDto update(@PathVariable(name = "commentId") long commentId,
                                      @RequestBody EventCommentRequestDto eventCommentRequestDto) {
        log.info("PATCH \"/admin/comments/{}\" Body={}", commentId, eventCommentRequestDto);
        CommentUpdateParam updateParam = CommentUpdateParam.builder()
                .commentId(commentId)
                .eventCommentRequestDto(eventCommentRequestDto)
                .build();

        EventCommentInfoDto eventRequestList = eventCommentService.update(updateParam);
        log.debug("EventRequest= updated=" + eventRequestList);
        return eventRequestList;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "commentId") long commentId) {
        log.info("DELETE \"/admin/comments/{}\"", commentId);
        CommentDeleteParam deleteParam = CommentDeleteParam.builder()
                .commentId(commentId)
                .build();

        eventCommentService.delete(deleteParam);
        log.debug("EventRequestList deleted with id=" + commentId);
    }
}
