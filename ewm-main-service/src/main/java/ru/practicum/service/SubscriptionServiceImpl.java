package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.subscription.ShortSubscriptionDtoForAuthor;
import ru.practicum.dto.subscription.ShortSubscriptionDtoForSubscriber;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.exp.NonExistentSubscriptionException;
import ru.practicum.exp.NonExistentUserException;
import ru.practicum.mapper.SubscriptionMapper;
import ru.practicum.model.Subscription;
import ru.practicum.model.User;
import ru.practicum.repository.SubscriptionRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    public SubscriptionDto createSubscription(Long userId, Long otherUserId) {
        User subscriber = userRepository.findById(userId).orElseThrow(() -> new NonExistentUserException(userId));
        User authorOfContent = userRepository.findById(otherUserId)
                .orElseThrow(() -> new NonExistentUserException(otherUserId));
        if (!authorOfContent.getPermission()) {
            throw new NonExistentUserException("Пользователь запретил подписки на себя.");
        }
        Subscription subscription = new Subscription(authorOfContent, subscriber);
        subscription.setCreated(LocalDateTime.now());
        return SubscriptionMapper.toSubscriptionDto(subscriptionRepository.save(subscription));
    }

    @Override
    public void deleteSubscriptionById(Long userId, Long subsId) {
        subscriptionRepository.deleteById(subsId);
    }

    @Override
    public List<ShortSubscriptionDtoForAuthor> findSubscriptionsByAuthorOfContentId(Long userId,
                                                                                    PageRequest pageRequest) {
        return subscriptionRepository.findByAuthorOfContentId(userId, pageRequest)
                .stream()
                .map(SubscriptionMapper::toShortSubscriptionDtoForAuthor)
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionDto findSubscriptionsById(Long userId, Long subsId) {
        return SubscriptionMapper.toSubscriptionDto(subscriptionRepository
                .findById(subsId).orElseThrow(() -> new NonExistentSubscriptionException(subsId)));
    }

    @Override
    public void deleteSubscriptionsByAuthorOfContentId(Long userId) {
        subscriptionRepository.deleteByAuthorOfContentId(userId);
    }

    @Override
    public List<ShortSubscriptionDtoForSubscriber> findSubscriptionsBySubscriberId(Long userId,
                                                                                   PageRequest pageRequest) {
        return subscriptionRepository.findBySubscriberId(userId, pageRequest)
                .stream()
                .map(SubscriptionMapper::toShortSubscriptionDtoForSubscriber)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSubscriptionsBySubscriberId(Long userId) {
        subscriptionRepository.deleteBySubscriberId(userId);
    }

    @Override
    public List<SubscriptionDto> findSubscriptionsByAdmin(PageRequest pageRequest) {
        return subscriptionRepository.findAll(pageRequest)
                .stream()
                .map(SubscriptionMapper::toSubscriptionDto)
                .collect(Collectors.toList());
    }
}
