package ru.practicum.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.subscription.ShortSubscriptionDtoForAuthor;
import ru.practicum.dto.subscription.ShortSubscriptionDtoForSubscriber;
import ru.practicum.dto.subscription.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDto createSubscription(Long userId, Long otherUserId);

    void deleteSubscriptionById(Long userId, Long otherUserId);

    List<ShortSubscriptionDtoForAuthor> findSubscriptionsByAuthorOfContentId(Long userId, PageRequest pageRequest);

    SubscriptionDto findSubscriptionsById(Long userId, Long subsId);

    void deleteSubscriptionsByAuthorOfContentId(Long userId);

    List<ShortSubscriptionDtoForSubscriber> findSubscriptionsBySubscriberId(Long userId, PageRequest pageRequest);

    void deleteSubscriptionsBySubscriberId(Long userId);

    List<SubscriptionDto> findSubscriptionsByAdmin(PageRequest pageRequest);
}
