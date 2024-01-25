package ru.practicum.mapper;

import ru.practicum.dto.subscription.ShortSubscriptionDtoForAuthor;
import ru.practicum.dto.subscription.ShortSubscriptionDtoForSubscriber;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.model.Subscription;

import java.time.format.DateTimeFormatter;

public class SubscriptionMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static SubscriptionDto toSubscriptionDto(Subscription subscription) {
        return new SubscriptionDto(
                subscription.getId(),
                UserMapper.toUserShortDto(subscription.getAuthorOfContent()),
                UserMapper.toUserShortDto(subscription.getSubscriber()),
                subscription.getCreated().format(formatter)
        );
    }

    public static ShortSubscriptionDtoForAuthor toShortSubscriptionDtoForAuthor(Subscription subscription) {
        return new ShortSubscriptionDtoForAuthor(
                subscription.getId(),
                UserMapper.toUserShortDto(subscription.getSubscriber()),
                subscription.getCreated().format(formatter)
        );
    }

    public static ShortSubscriptionDtoForSubscriber toShortSubscriptionDtoForSubscriber(Subscription subscription) {
        return new ShortSubscriptionDtoForSubscriber(
                subscription.getId(),
                UserMapper.toUserShortDto(subscription.getAuthorOfContent()),
                subscription.getCreated().format(formatter)
        );
    }
}
