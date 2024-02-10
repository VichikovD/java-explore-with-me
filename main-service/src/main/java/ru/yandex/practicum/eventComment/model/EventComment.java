package ru.yandex.practicum.eventComment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "event_comments")
public class EventComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_comment_id")
    Long id;

    @Column(name = "created")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    User author;

    @Column(name = "text")
    String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    Event event;

    @Column(name = "is_modified")
    boolean isModified;
}
