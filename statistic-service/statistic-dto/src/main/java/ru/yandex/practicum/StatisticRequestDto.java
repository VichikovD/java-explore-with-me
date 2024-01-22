package ru.yandex.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StatisticRequestDto {
    @NotBlank(message = "App should not be empty")
    private String app;

    @NotBlank(message = "Uri should not be empty")
    private String uri;

    @NotBlank(message = "Ip should not be empty")
    private String ip;

    @NotNull(message = "Timestamp should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
