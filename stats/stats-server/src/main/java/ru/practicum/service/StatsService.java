package ru.practicum.service;

import ru.practicum.hit.HitDto;
import ru.practicum.hit.HitDtoForSend;
import ru.practicum.hit.HitShort;

import java.util.List;

public interface StatsService {
    HitDtoForSend saveHit(HitDto hitDto);

    List<HitShort> getStats(String start, String end, String[] uris, boolean unique);
}
