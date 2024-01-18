package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.request.UpdateCompilationRequest;
import ru.practicum.exp.NonExistentCompilationException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(Arrays.asList(newCompilationDto.getEvents()));
            compilation.setEvents(events);
        }
        compilation = compilationRepository.save(compilation);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        if (compilation.getEvents() != null) {
            compilationDto.setEvents(compilation.getEvents()
                    .stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList()));
        }
        return compilationDto;
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto replaceCompilationById(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NonExistentCompilationException(compId));
        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(Arrays.asList(request.getEvents()));
            compilation.setEvents(events);
        }
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        compilation = compilationRepository.save(compilation);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        if (compilation.getEvents() != null) {
            compilationDto.setEvents(compilation.getEvents()
                    .stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList()));
        }
        return compilationDto;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NonExistentCompilationException(compId));
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        if (compilation.getEvents() != null) {
            compilationDto.setEvents(compilation.getEvents()
                    .stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList()));
        }
        return compilationDto;
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, PageRequest pageRequest) {
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, pageRequest)
                .stream()
                .collect(Collectors.toList());
        return compilations
                .stream()
                .map(compilation -> {
                    CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
                    compilationDto.setEvents(Optional.ofNullable(compilation.getEvents())
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(EventMapper::toEventShortDto)
                            .collect(Collectors.toList()));
                    return compilationDto;
                })
                .collect(Collectors.toList());
    }
}
