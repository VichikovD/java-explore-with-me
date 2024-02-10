package ru.yandex.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Entity(name = "statistics")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistic_id")
    Long id;

    @Column(name = "app")
    @NotBlank(message = "App should not be empty")
    String app;

    @Column(name = "uri")
    @NotBlank(message = "Uri should not be empty")
    String uri;

    @Column(name = "ip")
    @NotBlank(message = "Ip should not be empty")
    String ip;

    @NotNull(message = "Timestamp should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created")
    LocalDateTime timestamp;
}
