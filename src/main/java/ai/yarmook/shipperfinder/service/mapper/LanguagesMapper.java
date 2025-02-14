package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.Languages;
import ai.yarmook.shipperfinder.service.dto.LanguagesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Languages} and its DTO {@link LanguagesDTO}.
 */
@Mapper(componentModel = "spring")
public interface LanguagesMapper extends EntityMapper<LanguagesDTO, Languages> {}
