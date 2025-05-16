package ru.practicum.main.service.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.practicum.main.service.compilation.dto.CompilationDto;
import ru.practicum.main.service.compilation.dto.NewCompilationDto;
import ru.practicum.main.service.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main.service.compilation.model.Compilation;
import ru.practicum.main.service.event.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapperCompilation {
    @Mapping(source = "events", target = "events", qualifiedByName = "saveOnlyIds")
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    @Mapping(source = "events", target = "events", qualifiedByName = "saveOnlyIds")
    Compilation toCompilation(UpdateCompilationRequest updateCompilationRequest);

    CompilationDto toCompilationDto(Compilation compilation);

    @Named("saveOnlyIds")
    default Set<Event> saveOnlyIds(Set<Long> ids) {
        if (ids == null) {
            return null;
        }

        return ids.stream()
                .map(id -> {
                    Event event = new Event();
                    event.setId(id);
                    return event;
                }).collect(Collectors.toSet());
    }
}
