package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestBody ItemRequestDtoRequest request) {
        return requestService.createRequest(userId, request);
    }

    @GetMapping
    public  List<ItemRequestDto> getRequestsByRequestorId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getRequestsByRequestorId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getByRequestId(@PathVariable Long requestId) {
        return requestService.getByRequestId(requestId);
    }
}
