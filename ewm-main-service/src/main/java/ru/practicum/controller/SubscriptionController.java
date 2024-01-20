package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.subscription.ShortSubscriptionDtoForAuthor;
import ru.practicum.dto.subscription.ShortSubscriptionDtoForSubscriber;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.service.SubscriptionService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/users/{userId}/subscriptions/{otherUserId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto createSubscription(@PathVariable(name = "userId") @PositiveOrZero Long userId,
                                              @PathVariable(name = "otherUserId") @PositiveOrZero Long otherUserId) {
        return subscriptionService.createSubscription(userId, otherUserId);
    }

    @DeleteMapping("/users/{userId}/subscriptions/{subsId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscriptionById(@PathVariable(name = "userId") @PositiveOrZero Long userId,
                                       @PathVariable(name = "subsId") @PositiveOrZero Long subsId) {
        subscriptionService.deleteSubscriptionById(userId, subsId);
    }

    @GetMapping("/users/{userId}/subscriptions/incoming")
    public List<ShortSubscriptionDtoForAuthor> findSubscriptionsByUserId(@PathVariable(name = "userId")
                                                                         @PositiveOrZero Long userId,
                                                                         @RequestParam(required = false, name = "from",
                                                                                 defaultValue = "0")
                                                                         @PositiveOrZero int from,
                                                                         @RequestParam(required = false, name = "size",
                                                                                 defaultValue = "10")
                                                                         @Positive int size) {
        return subscriptionService.findSubscriptionsByAuthorOfContentId(userId, PageRequest.of(from / size, size));
    }

    @GetMapping("/users/{userId}/subscriptions/{subsId}")
    public SubscriptionDto findSubscriptionsByUserIdAndSubscriptionId(@PathVariable(name = "userId")
                                                                      @PositiveOrZero Long userId,
                                                                      @PathVariable(name = "subsId")
                                                                      @PositiveOrZero Long subsId) {
        return subscriptionService.findSubscriptionsById(userId, subsId);
    }

    @DeleteMapping("/users/{userId}/subscriptions/incoming")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscriptionsByAuthorOfContentId(@PathVariable(name = "userId") @PositiveOrZero Long userId) {
        subscriptionService.deleteSubscriptionsByAuthorOfContentId(userId);
    }

    @GetMapping("/users/{userId}/subscriptions/outgoing")
    List<ShortSubscriptionDtoForSubscriber> findSubscriptionsBySubscriberId(@PathVariable(name = "userId")
                                                                            @PositiveOrZero Long userId,
                                                                            @RequestParam(required = false,
                                                                                    name = "from", defaultValue = "0")
                                                                            @PositiveOrZero int from,
                                                                            @RequestParam(required = false,
                                                                                    name = "size", defaultValue = "10")
                                                                            @Positive int size) {
        return subscriptionService.findSubscriptionsBySubscriberId(userId, PageRequest.of(from / size, size));
    }

    @DeleteMapping("/users/{userId}/subscriptions/outgoing")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscriptionsBySubscriberId(@PathVariable(name = "userId") @PositiveOrZero Long userId) {
        subscriptionService.deleteSubscriptionsBySubscriberId(userId);
    }

    @GetMapping("/admin/subscriptions")
    public List<SubscriptionDto> findSubscriptionsByAdmin(@RequestParam(required = false, name = "from",
                                                                  defaultValue = "0") @PositiveOrZero int from,
                                                          @RequestParam(required = false, name = "size",
                                                                  defaultValue = "10") @Positive int size) {
        return subscriptionService.findSubscriptionsByAdmin(PageRequest.of(from / size, size));
    }
}
