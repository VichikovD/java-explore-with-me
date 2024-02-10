package ru.yandex.practicum.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserDto {
    private Long id;

    @Size(min = 2, max = 250)
    @NotBlank(message = "Field: name. Error: must not be blank.")
    private String name;


    @Size(min = 6, max = 254, message = "Field: email. Error: length must be minimum 6 and maximum 254.")
    @NotBlank(message = "Field: email. Error: must not be blank.")
    @Email
    private String email;
}
