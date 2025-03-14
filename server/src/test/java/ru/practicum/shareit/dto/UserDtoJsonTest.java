package ru.practicum.shareit.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoJsonTest {
    private final JacksonTester<UserDto> json;
    private final JacksonTester<UpdateUserRequest> jsonUpdate;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).name("Vladimir").email("gorych@mail.ru").build();
        JsonContent<UserDto> result = json.write(userDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Vladimir");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("gorych@mail.ru");
    }

    @Test
    void testUserUpdateUserRequest() throws Exception {
        UpdateUserRequest update = UpdateUserRequest.builder().name("Vladimir").email("gorych@mail.ru").build();
        JsonContent<UpdateUserRequest> result = jsonUpdate.write(update);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Vladimir");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("gorych@mail.ru");
    }
}
