package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoUser;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public ItemDto createItem(Long userId, ItemDtoRequest item) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь " +
                "с id = " + userId + " не найден"));
        ItemRequest request = null;
        if (item.getRequestId() != null) {
            request = requestRepository.findById(item.getRequestId()).orElseThrow(null);
        }
        Item newItem = itemRepository.save(ItemMapper.mapToItem(item, owner, request));
        log.info("Вещь с id {} добавлена в сервис", newItem.getId());
        return ItemMapper.mapToItemDto(newItem);
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDtoRequest request) {
        User author = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь " +
                "с id = " + userId + " не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь " +
                "с id = " + itemId + " не найдена"));
        LocalDateTime now = LocalDateTime.now();
        log.info("now = {}", now);
        Booking booking = bookingRepository.findByItemIdAndEndTimeBefore(item, now);
        if (booking == null || !booking.getBooker().getId().equals(userId)) {
            throw new ValidationException("Пользователь " + userId + " не брал в аренду вещь " + itemId);
        }
        Comment comment = CommentMapper.mapToComment(request, item, author);
        commentRepository.save(comment);
        return CommentMapper.mapToCommentDto(comment);
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long userId, Long itemId, Item item) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь " +
                "с id = " + userId + " не найден"));
        Item updateItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь " +
                "с id = " + itemId + " не найдена"));
        if (!updateItem.getOwner().getId().equals(userId)) {
            throw new ValidationException("Пользователь с id " + userId +
                    " не является владельцем вещи с id " + itemId);
        }
        if (item.getName() != null && item.getDescription() != null && item.getAvailable() != null) {
            updateItem = ItemMapper.mapToUpdateItemAllFields(updateItem, item);
        } else if (item.getName() != null && item.getDescription() != null) {
            updateItem = ItemMapper.mapToUpdateItemNameAndDescription(updateItem, item);
        } else if (item.getName() != null && item.getAvailable() != null) {
            updateItem = ItemMapper.mapToUpdateItemNameAndAvailable(updateItem, item);
        } else if (item.getAvailable() != null && item.getDescription() != null) {
            updateItem = ItemMapper.mapToUpdateItemAvailableAndDescription(updateItem, item);
        } else if (item.getName() != null) {
            updateItem = updateItem.toBuilder().name(item.getName()).build();
        } else if (item.getDescription() != null) {
            updateItem = updateItem.toBuilder().description(item.getDescription()).build();
        } else if (item.getAvailable() != null) {
            updateItem = updateItem.toBuilder().available(item.getAvailable()).build();
        }
        itemRepository.save(updateItem);
        log.info("Вещь с id {} обновлена в сервисе", itemId);
        return ItemMapper.mapToItemDto(updateItem);
    }

    @Override
    public List<ItemDtoOwner> getAllItemByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь " +
                "с id = " + userId + " не найден"));
        List<Item> itemList = itemRepository.findByOwnerId(userId);
        List<Long> itemIds = itemList.stream().map(Item::getId).toList();
        List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStartDesc(itemIds);
        List<Comment> comments = commentRepository.findAllByItemId(itemIds);
        Map<Long, List<Booking>> bookingGroup = bookings.stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
        Map<Long, List<CommentDto>> commentGroup =  comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId(),
                        Collectors.mapping(CommentMapper::mapToCommentDto, Collectors.toList())));
        return itemList.stream()
                .map(item -> {
                    List<Booking> bookingList = bookingGroup.getOrDefault(item.getId(), Collections.emptyList());
                    BookingDto bookingLast = bookingList
                            .stream()
                            .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                            .reduce((first, second) -> second)
                            .map(BookingMapper::mapToBookingDto)
                            .orElse(null);
                    BookingDto bookingNext = bookingList
                            .stream()
                            .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                            .findFirst()
                            .map(BookingMapper::mapToBookingDto)
                            .orElse(null);

                    return ItemMapper.mapToItemDtoOwner(item, bookingLast, bookingNext,
                            commentGroup.getOrDefault(item.getId(), Collections.emptyList())
                    );
                })
                .toList();
    }

    @Override
    public ItemDtoUser getByIdItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь " +
                "с id = " + itemId + " не найдена"));
        List<Comment> comments = commentRepository.findByItemId(itemId);
        List<CommentDto> commentsDto = comments.stream().map(CommentMapper::mapToCommentDto).toList();
        return ItemMapper.mapToItemDtoUser(item, commentsDto);
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        List<Item> itemList = new ArrayList<>();
        if (!text.isBlank()) {
            itemList = itemRepository.search(text);
        }
        return itemList.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }
}
