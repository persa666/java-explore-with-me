package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exp.TimeException;
import ru.practicum.hit.HitDto;
import ru.practicum.hit.HitDtoForSend;
import ru.practicum.hit.HitMapper;
import ru.practicum.hit.HitShort;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public HitDtoForSend saveHit(HitDto hitDto) {
        return HitMapper.toHitDtoForSend(statsRepository.save(HitMapper.toHit(hitDto)));
    }

    @Override
    public List<HitShort> getStats(String start, String end, String[] uris, boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (LocalDateTime.parse(start, formatter).isAfter(LocalDateTime.parse(end, formatter))) {
            throw new TimeException("Дата начала поиска позже даты конца.");
        }
        if (unique) {
            if (uris.length > 0) {
                return statsRepository.findByTimestampBetweenAndUriInDistinct(LocalDateTime.parse(start, formatter),
                        LocalDateTime.parse(end, formatter), uris);
            } else {
                return statsRepository.findByTimestampBetweenDistinct(LocalDateTime.parse(start, formatter),
                        LocalDateTime.parse(end, formatter));
            }
        } else {
            if (uris.length > 0) {
                return statsRepository.findByTimestampBetweenAndUriIn(LocalDateTime.parse(start, formatter),
                        LocalDateTime.parse(end, formatter), uris);
            } else {
                return statsRepository.findByTimestampBetween(LocalDateTime.parse(start, formatter),
                        LocalDateTime.parse(end, formatter));
            }
        }
    }
}
