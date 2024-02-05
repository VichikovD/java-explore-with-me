package ru.yandex.practicum.compilation.model.dto;

import lombok.*;
import ru.yandex.practicum.validation.NotBlankIfNotNull;

import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CompilationRequestDto {
    Boolean pinned;

    @NotBlankIfNotNull
    @Size(min = 1, max = 50)
    String title;

    Set<Long> events;
}
