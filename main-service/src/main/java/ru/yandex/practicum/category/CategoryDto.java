package ru.yandex.practicum.category;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Field: name. Error: must not be blank.")
    private String name;

}
