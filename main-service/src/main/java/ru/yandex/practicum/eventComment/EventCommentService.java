package ru.yandex.practicum.eventComment;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventCommentService {
    EventCommentInfoDto create(long authorId, long eventId, EventCommentRequestDto eventCommentRequestDto);

    EventCommentInfoDto updateWithUserIdValidation(long authorId, long eventId, EventCommentRequestDto eventCommentRequestDto);

    EventCommentInfoDto update(long eventId, EventCommentRequestDto eventCommentRequestDto);

    void deleteWithAuthorIdValidation(long authorId, long commentId);

    void delete(long commentId);

    List<EventCommentInfoDto> getByAuthorId(long authorId, Pageable pageable);

    List<EventCommentInfoDto> getFiltered(GetEventCommentsRequest getEventCommentsRequest);
}
