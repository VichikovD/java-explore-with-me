package ru.yandex.practicum.compilation.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CompilationCreateDto {

    boolean pinned = false;

    @NotBlank
    @Size(max = 50)
    String title;

    Set<Long> events = new HashSet<>();
}