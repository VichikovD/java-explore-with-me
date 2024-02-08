package ru.yandex.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.PublishState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Set<Event> findByIdIn(Collection<Long> idCollection);

    List<Event> findByInitiatorId(long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(long initiatorId, long eventId);

    Optional<Event> findByIdAndState(long eventId, PublishState state);

    @Query(value = "SELECT e " +
            "FROM Event AS e " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN (:users)) " +
            "AND ((:states) IS NULL OR e.state IN (:states)) " +
            "AND ((:categories) IS NULL OR e.category.id IN (:categories)) " +
            "AND (" +
            "cast((:rangeStart) AS timestamp) IS NULL " +
            "OR cast((:rangeEnd) AS timestamp) IS NULL " +
            "OR (e.eventDate BETWEEN (:rangeStart) AND (:rangeEnd))" +
            ")")
    List<Event> findFilteredAsAdmin(Collection<Long> users, Collection<PublishState> states, Collection<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT e " +
            "FROM Event AS e " +
            "LEFT JOIN FETCH e.category AS c " +
            "LEFT JOIN FETCH e.initiator AS i " +
            "WHERE ((:text) IS NULL OR (LOWER(e.annotation) LIKE(LOWER(CONCAT('%', (:text), '%'))) OR LOWER(e.description) LIKE(LOWER(CONCAT('%', (:text), '%'))))) " +
            "AND ((:categories) IS NULL OR e.category.id IN (:categories)) " +
            "AND ((:paid) IS NULL OR e.paid = (:paid)) " +
            "AND (" +
            "(cast((:rangeStart) AS timestamp) IS NULL OR cast((:rangeEnd) AS timestamp) IS NULL OR (e.eventDate BETWEEN (:rangeStart) AND (:rangeEnd)))" +
            "OR ((cast((:rangeStart) AS timestamp) IS NULL OR cast((:rangeEnd) AS timestamp) IS NULL) AND e.eventDate > NOW())" +
            ") " +
            "AND (e.state = 'PUBLISHED')")
    List<Event> findFilteredAsUser(
            @Param("text") String text,
            @Param("categories") Collection<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);
}
