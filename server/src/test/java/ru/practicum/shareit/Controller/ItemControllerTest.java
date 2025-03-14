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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.dto.ItemDtoUser;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private ItemService service;
    @InjectMocks
    private ItemController controller;
    private MockMvc mvc;
    private ItemDto itemDto;
    private ItemDtoOwner itemDtoOwner;
    private ItemDtoUser itemDtoUser;
    private User owner;
    private CommentDto commentDto;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        owner = User.builder().id(1L).name("Vladimir").email("gorych@mail.ru").build();
        itemDto = ItemDto.builder().id(2L).name("name").description("description").owner(owner).available(true).build();
        itemDtoOwner = ItemDtoOwner.builder().id(2L).name("name").description("description").owner(owner).available(true).build();
        itemDtoUser = ItemDtoUser.builder().id(2L).name("name").description("description").owner(owner).available(true).build();
        commentDto = CommentDto.builder().text("text").id(3L).authorName(owner.getName()).build();
    }

    @Test
    void createItem_shouldIsOk() throws Exception {
        when(service.createItem(anyLong(), any()))
                .thenReturn(itemDto);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", owner.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner.id", is(itemDto.getOwner().getId()), Long.class))
                .andExpect(jsonPath("$.owner.name", is(itemDto.getOwner().getName())))
                .andExpect(jsonPath("$.owner.email", is(itemDto.getOwner().getEmail())));
    }

    @Test
    void updateItem_shouldIsOk() throws Exception {
        when(service.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);
        mvc.perform(patch("/items/" + itemDto.getId())
                        .header("X-Sharer-User-Id", owner.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner.id", is(itemDto.getOwner().getId()), Long.class))
                .andExpect(jsonPath("$.owner.name", is(itemDto.getOwner().getName())))
                .andExpect(jsonPath("$.owner.email", is(itemDto.getOwner().getEmail())));
    }

    @Test
    void getAllItemsByUserId_shouldIsOk() throws Exception {
        when(service.getAllItemByUserId(anyLong()))
                .thenReturn(List.of(itemDtoOwner));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", owner.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id", is(itemDto.getId()), long.class))
                .andExpect(jsonPath("[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("[0].owner.id", is(itemDto.getOwner().getId()), Long.class))
                .andExpect(jsonPath("[0].owner.name", is(itemDto.getOwner().getName())))
                .andExpect(jsonPath("[0].owner.email", is(itemDto.getOwner().getEmail())));
    }

    @Test
    void getByIdItem_shouldIsOk() throws Exception {
        when(service.getByIdItem(anyLong()))
                .thenReturn(itemDtoUser);

        mvc.perform(get("/items/" + itemDto.getId())
                        .header("X-Sharer-User-Id", owner.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner.id", is(itemDto.getOwner().getId()), Long.class))
                .andExpect(jsonPath("$.owner.name", is(itemDto.getOwner().getName())))
                .andExpect(jsonPath("$.owner.email", is(itemDto.getOwner().getEmail())));
    }

    @Test
    void getSearch_shouldIsOk() throws Exception {
        when(service.getSearch(anyString()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("text", "dfg")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), long.class))
                .andExpect(jsonPath("[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("[0].owner.id", is(itemDto.getOwner().getId()), Long.class))
                .andExpect(jsonPath("[0].owner.name", is(itemDto.getOwner().getName())))
                .andExpect(jsonPath("[0].owner.email", is(itemDto.getOwner().getEmail())));
    }

    @Test
    void addComment_shouldIsOk() throws Exception {
        when(service.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/" + itemDto.getId() + "/comment")
                        .header("X-Sharer-User-Id", owner.getId())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.item", is(commentDto.getItem())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName()), String.class))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated())));
    }
}
