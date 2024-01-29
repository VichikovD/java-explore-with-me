package ru.yandex.practicum.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.category.Category;
import ru.yandex.practicum.event.dto.PublishState;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.util.Location;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long id;

    @Column(name = "annotation")
    String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    /*
    @OneToMany(mappedBy = "event")
    Set<EventRequest> confirmedRequests;
    */

    @Column(name = "created_on")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    @Column(name = "description")
    String description;

    @Column(name = "event_date")
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id")
    Location location;

    @Column(name = "paid")
    boolean paid;

    @Column(name = "participant_limit")
    int participantLimit;

    @Column(name = "published_on")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    boolean requestModeration;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    PublishState state;

    @Column(name = "title")
    String title;

    /*
    @Column(name = "views")
    long views;
    */
}
