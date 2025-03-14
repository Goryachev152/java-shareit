package ru.practicum.shareit.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @Mock
    private RequestService service;

    @InjectMocks
    private ItemRequestController controller;

    private MockMvc mvc;

    private ItemRequestDto requestDto;

    private User user;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        user = User.builder().id(1L).name("Vladimir").email("gorych@mail.ru").build();
        requestDto = ItemRequestDto.builder().id(1L).requestor(user).description("description").build();
    }

    @Test
    void createRequest_shouldIsOk() throws Exception {
        when(service.createRequest(anyLong(), any(ItemRequestDtoRequest.class)))
                .thenReturn(requestDto);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(requestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$.items", is(requestDto.getItems())));
    }

    @Test
    void getByRequestId_shouldIsOk() throws Exception {
        when(service.getByRequestId(anyLong()))
                .thenReturn(requestDto);
        mvc.perform(get("/requests/" + requestDto.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(requestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$.items", is(requestDto.getItems())));
    }

    @Test
    void getRequestsByRequestorId_shouldIsOk() throws Exception {
        when(service.getRequestsByRequestorId(anyLong()))
                .thenReturn(List.of(requestDto));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].requestor.id", is(requestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$[0].created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$[0].items", is(requestDto.getItems())));
    }

    @Test
    void getAllRequests_shouldIsOk() throws Exception {
        when(service.getAllRequests())
                .thenReturn(List.of(requestDto));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].requestor.id", is(requestDto.getRequestor().getId()), Long.class))
                .andExpect(jsonPath("$[0].created", is(requestDto.getCreated())))
                .andExpect(jsonPath("$[0].items", is(requestDto.getItems())));
    }
}