package ru.yandex.practicum.eventComment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventCommentRepository extends JpaRepository<EventComment, Long> {
    Optional<EventComment> findByIdAndAuthorId(long id, long authorId);

    List<EventComment> findByAuthorId(long authorId, Pageable pageable);

    @Query(value = "SELECT c " +
            "FROM EventComment AS c " +
            "WHERE ((:events) IS NULL OR c.event.id IN (:events)) " +
            "AND (" +
            "cast((:rangeStart) AS timestamp) IS NULL " +
            "OR cast((:rangeEnd) AS timestamp) IS NULL " +
            "OR (c.createdOn BETWEEN (:rangeStart) AND (:rangeEnd))" +
            ")")
    List<EventComment> findFilteredAsAdmin(Collection<Long> events, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
