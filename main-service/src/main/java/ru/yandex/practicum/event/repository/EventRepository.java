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

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(long initiatorId, long eventId);

    Optional<Event> findByIdAndState(long eventId, PublishState state);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
            Collection<Long> users, Collection<PublishState> state, Collection<Long> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM events AS e " +
            "LEFT JOIN categories AS c ON e.category_id = c.category_id " +
            "LEFT JOIN users AS u ON e.initiator_id = u.user_id " +
            "LEFT JOIN event_requests AS er ON er.event_id = e.event_id " +
            //        "JOIN FETCH e.location " +
            "WHERE (LOWER(e.annotation) LIKE(LOWER(CONCAT('%', :text, '%'))) OR " +
            "LOWER(e.description) LIKE(LOWER(CONCAT('%', :text, '%')))) AND " +
            "(e.category_id IN :categories) AND " +
            "(e.paid = :paid) AND " +
            "(e.event_date > NOW()) AND " +
            "(e.state = 'PUBLISHED') " +
            "(er.status = 'CONFIRMED') " +
            "GROUP BY e.event_id " +
            "HAVING COUNT(er.event_id) < e.participant_limit", nativeQuery = true)
    List<Event> findAllAvailableFilteredStartingFromToday(
            @Param("text") String text,
            @Param("categories") Collection<Long> categories,
            @Param("paid") boolean paid,
            Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM events AS e " +
            "LEFT JOIN categories AS c ON e.category_id = c.category_id " +
            "LEFT JOIN users AS u ON e.initiator_id = u.user_id " +
            //        "JOIN FETCH e.location " +
            "WHERE (LOWER(e.annotation) LIKE(LOWER(CONCAT('%', :text, '%'))) OR " +
            "LOWER(e.description) LIKE(LOWER(CONCAT('%', :text, '%')))) AND " +
            "(e.category_id IN :categories) AND " +
            "(e.paid = :paid) AND " +
            "(e.event_date > NOW()) AND " +
            "(e.state = 'PUBLISHED')", nativeQuery = true)
    List<Event> findAllFilteredStartingFromToday(
            @Param("text") String text,
            @Param("categories") Collection<Long> categories,
            @Param("paid") boolean paid,
            Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM events AS e " +
            "LEFT JOIN categories AS c ON e.category_id = c.category_id " +
            "LEFT JOIN users AS u ON e.initiator_id = u.user_id " +
            "LEFT JOIN event_requests AS er ON er.event_id = e.event_id " +
            //        "JOIN FETCH e.location " +
            "WHERE (LOWER(e.annotation) LIKE(LOWER(CONCAT('%', :text, '%'))) OR " +
            "LOWER(e.description) LIKE(LOWER(CONCAT('%', :text, '%')))) AND " +
            "(e.category_id IN :categories) AND " +
            "(e.paid = :paid) AND " +
            "(e.event_date > :rangeStart) AND " +
            "(e.event_date < :rangeEnd) AND " +
            "(e.state = 'PUBLISHED') " +
            "(er.status = 'CONFIRMED') " +
            "GROUP BY e.event_id " +
            "HAVING COUNT(er.event_id) < e.participant_limit", nativeQuery = true)
    List<Event> findAllAvailableFiltered(
            @Param("text") String text,
            @Param("categories") Collection<Long> categories,
            @Param("paid") boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM events AS e " +
            "LEFT JOIN categories AS c ON e.category_id = c.category_id " +
            "LEFT JOIN users AS u ON e.initiator_id = u.user_id " +
            //        "JOIN FETCH e.location " +
            "WHERE (LOWER(e.annotation) LIKE(LOWER(CONCAT('%', :text, '%'))) OR " +
            "LOWER(e.description) LIKE(LOWER(CONCAT('%', :text, '%')))) AND " +
            "(e.category_id IN :categories) AND " +
            "(e.paid = :paid) AND " +
            "(e.event_date > :rangeStart) AND " +
            "(e.event_date < :rangeEnd) AND " +
            "(e.state = 'PUBLISHED') ", nativeQuery = true)
    List<Event> findAllFiltered(
            @Param("text") String text,
            @Param("categories") Collection<Long> categories,
            @Param("paid") boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    /*
            "SELECT b " +
            "FROM Booking AS b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.id = ?1 AND (b.item.owner.id = ?2 OR b.booker.id = ?2)"
    */

    // -------------------------

    /*
    String text,
    int[] categories,
    boolean paid,
    String rangeStart,
    String rangeEnd,
    boolean onlyAvailable,
    Pageable pageable
                                              */
}
