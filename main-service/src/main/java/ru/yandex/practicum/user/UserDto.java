package ru.yandex.practicum.user;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "Field: name. Error: must not be blank.")
    private String name;

    // В спецификации нет валидации email, но я бы сделал
    @NotBlank(message = "Field: email. Error: must not be blank.")
    private String email;
}
