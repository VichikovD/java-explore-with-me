package ru.yandex.practicum.category;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CategoryDto {
    private Long id;

    @NotEmpty(message = "Name cannot be null or empty")
    private String name;
}
