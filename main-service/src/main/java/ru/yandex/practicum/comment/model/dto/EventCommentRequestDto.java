package ru.yandex.practicum.comment.model.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EventCommentRequestDto {
    @NotEmpty(message = "Field: description. Error: must not be empty if not empty.")
    @Size(max = 10000)
    String text;
}
