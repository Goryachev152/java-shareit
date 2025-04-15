package ru.practicum.shareit.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoUser;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private RequestRepository requestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    private User user;
    private ItemDto itemDto;
    private Item item;
    private ItemDtoRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Vladimir", "gorych@mail.ru");
        itemRequest = ItemDtoRequest.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
        item = ItemMapper.mapToItem(itemRequest, user, null);
        item = item.toBuilder().id(1L).build();
        itemDto = ItemMapper.mapToItemDto(item);
    }

    @Test
    void addItem_ShouldReturnItemDto() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        ItemDto result = itemService.createItem(user.getId(), itemRequest);
        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getName(), result.getName());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void addItem_ShouldThrowNotFoundExceptionUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemService.createItem(user.getId(), itemRequest);
        });
        assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(itemRepository, never()).save(any(Item.class));
    }


    @Test
    void updateItem_ShouldReturnUpdatedItem() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Item updatedItem = Item.builder().name("newName").description("newDescription").build();
        ItemDto result = itemService.updateItem(user.getId(), item.getId(), updatedItem);
        assertNotNull(result);
        assertEquals(updatedItem.getName(), result.getName());
        assertEquals(updatedItem.getDescription(), result.getDescription());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldReturnUpdatedItemNewName() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Item updatedItem = Item.builder().name("newName").build();
        ItemDto result = itemService.updateItem(user.getId(), item.getId(), updatedItem);
        assertNotNull(result);
        assertEquals(updatedItem.getName(), result.getName());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldReturnUpdatedItemNewDescription() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Item updatedItem = Item.builder().description("newDescription").build();
        ItemDto result = itemService.updateItem(user.getId(), item.getId(), updatedItem);
        assertNotNull(result);
        assertEquals(updatedItem.getDescription(), result.getDescription());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldReturnUpdatedItemNewAvailable() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Item updatedItem = Item.builder().available(false).build();
        ItemDto result = itemService.updateItem(user.getId(), item.getId(), updatedItem);
        assertNotNull(result);
        assertEquals(updatedItem.getAvailable(), result.getAvailable());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldReturnUpdatedItemNewAllFields() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Item updatedItem = Item.builder().name("newName").description("newDescription").available(false).build();
        ItemDto result = itemService.updateItem(user.getId(), item.getId(), updatedItem);
        assertNotNull(result);
        assertEquals(updatedItem.getAvailable(), result.getAvailable());
        assertEquals(updatedItem.getName(), result.getName());
        assertEquals(updatedItem.getDescription(), result.getDescription());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldReturnUpdatedItemNewNameAndAvailable() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Item updatedItem = Item.builder().name("newName").available(false).build();
        ItemDto result = itemService.updateItem(user.getId(), item.getId(), updatedItem);
        assertNotNull(result);
        assertEquals(updatedItem.getAvailable(), result.getAvailable());
        assertEquals(updatedItem.getName(), result.getName());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldReturnUpdatedItemNewDescriptionAndAvailable() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Item updatedItem = Item.builder().description("newDescription").available(false).build();
        ItemDto result = itemService.updateItem(user.getId(), item.getId(), updatedItem);
        assertNotNull(result);
        assertEquals(updatedItem.getAvailable(), result.getAvailable());

        assertEquals(updatedItem.getDescription(), result.getDescription());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldThrowNotFoundExceptionUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        Item updatedItem = Item.builder().name("newName").description("newDescription").build();
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(user.getId(), item.getId(), updatedItem);
        });
        assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void getByIdItem_ShouldReturnItemDtoUser() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        ItemDtoUser result = itemService.getByIdItem(item.getId());
        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void getByIdItem_ShouldThrowNotFoundExceptionItem() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemService.getByIdItem(item.getId());
        });
        assertEquals("Вещь с id = 1 не найдена", exception.getMessage());
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void getAllItemByUserId_ShouldReturnListItemDto() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(Collections.singletonList(item));
        List<ItemDtoOwner> result = itemService.getAllItemByUserId(user.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getId(), result.get(0).getId());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findByOwnerId(user.getId());
    }

    @Test
    void getAllItemByUserId_ShouldThrowNotFoundExceptionUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemService.getAllItemByUserId(user.getId());
        });
        assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(itemRepository, never()).findByOwnerId(anyLong());
    }

    @Test
    void search_ShouldReturnListItemDto() {
        String searchText = "Вещь";
        when(itemRepository.search(searchText)).thenReturn(Collections.singletonList(item));
        List<ItemDto> result = itemService.getSearch(searchText);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getId(), result.get(0).getId());
        verify(itemRepository).search(searchText);
    }

    @Test
    void search_ShouldReturnEmptyList() {
        List<ItemDto> result = itemService.getSearch("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void addComment_ShouldReturnCommentDto() {
        CommentDtoRequest newCommentRequest = new CommentDtoRequest();
        newCommentRequest.setText("comment");
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText(newCommentRequest.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Booking booking = new Booking(1L, LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), item, user, null);
        when(bookingRepository.findByItemIdAndEndTimeBefore(any(Item.class), any(LocalDateTime.class)))
                .thenReturn(booking);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        CommentDto result = itemService.addComment(user.getId(), item.getId(), newCommentRequest);
        assertNotNull(result);
        assertEquals(comment.getText(), result.getText());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findById(item.getId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void comment_ShouldThrowValidationException() {
        CommentDtoRequest newCommentRequest = new CommentDtoRequest();
        newCommentRequest.setText("comment");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            itemService.addComment(user.getId(), item.getId(), newCommentRequest);
        });
        assertEquals("Пользователь 1 не брал в аренду вещь 1", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findById(item.getId());
    }
}
