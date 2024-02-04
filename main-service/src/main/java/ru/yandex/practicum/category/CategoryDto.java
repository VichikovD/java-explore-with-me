package ru.yandex.practicum.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CategoryDto {
    private Long id;

    @Size(max = 50)
    @NotBlank(message = "Field: name. Error: must not be blank.")
    private String name;

}
