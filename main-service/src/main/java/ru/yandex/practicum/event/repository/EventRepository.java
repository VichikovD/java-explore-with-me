package ru.yandex.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.PublishState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Set<Event> findAllByIdIn(Collection<Long> idCollection);

    List<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(long initiatorId, long eventId);

    Optional<Event> findByIdAndState(long eventId, PublishState state);

    @Query(value = "SELECT e " +
            "FROM Event AS e " +
            "JOIN FETCH e.category AS c " +
            "JOIN FETCH e.initiator AS i " +
            "JOIN FETCH e.location AS l " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN (:users)) " +
            "AND ((:states) IS NULL OR e.state IN (:states)) " +
            "AND ((:categories) IS NULL OR e.category.id IN (:categories)) " +
            "AND (" +
            "cast(:rangeStart AS timestamp) IS NULL " +
            "OR cast(:rangeEnd AS timestamp) IS NULL " +
            "OR (e.eventDate BETWEEN :rangeStart AND :rangeEnd)" +
            ")")
    List<Event> findAllFilteredAsAdmin(Collection<Long> users,
                                       Collection<PublishState> states,
                                       Collection<Long> categories,
                                       LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd,
                                       Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM events AS e " +
            "LEFT JOIN categories AS c ON e.category_id = c.category_id " +
            "LEFT JOIN users AS u ON e.initiator_id = u.user_id " +
            "LEFT JOIN event_requests AS er ON er.event_id = e.event_id " +
            //        "JOIN FETCH e.location " +
            "WHERE (" +
            ":text IS NULL " +
            "OR (LOWER(e.annotation) LIKE(LOWER(CONCAT('%', :text, '%'))) OR LOWER(e.description) LIKE(LOWER(CONCAT('%', :text, '%'))))" +
            ") " +
            "AND ((:categories) IS NULL OR e.category_id IN (:categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (" +
            "(cast(:rangeStart AS timestamp) IS NULL OR cast(:rangeEnd AS timestamp) IS NULL OR (e.event_date BETWEEN :rangeStart AND :rangeEnd))" +
            "OR ((cast(:rangeStart AS timestamp) IS NULL OR cast(:rangeEnd AS timestamp) IS NULL) AND e.event_date > NOW())" +
            ") " +
            "AND (e.state = 'PUBLISHED') " +
            "AND (er.status = 'CONFIRMED') " +
            "GROUP BY e.event_id " +
            "HAVING COUNT(er.event_id) < e.participant_limit", nativeQuery = true)
    List<Event> findAllAvailableFilteredAsUser(
            String text,
            Collection<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM events AS e " +
            "LEFT JOIN categories AS c ON e.category_id = c.category_id " +
            "LEFT JOIN users AS u ON e.initiator_id = u.user_id " +
            //        "JOIN FETCH e.location " +
            "WHERE (" +
            ":text IS NULL " +
            "OR (LOWER(e.annotation) LIKE(LOWER(CONCAT('%', :text, '%'))) OR LOWER(e.description) LIKE(LOWER(CONCAT('%', :text, '%'))))" +
            ") " +
            "AND ((:categories) IS NULL OR e.category_id IN (:categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (" +
            "(cast(:rangeStart AS timestamp) IS NULL OR cast(:rangeEnd AS timestamp) IS NULL OR (e.event_date BETWEEN :rangeStart AND :rangeEnd))" +
            "OR ((cast(:rangeStart AS timestamp) IS NULL OR cast(:rangeEnd AS timestamp) IS NULL) AND e.event_date > NOW())" +
            ") " +
            "AND (e.state = 'PUBLISHED') ", nativeQuery = true)
    List<Event> findAllFilteredAsUser(
            String text,
            Collection<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable);
}
