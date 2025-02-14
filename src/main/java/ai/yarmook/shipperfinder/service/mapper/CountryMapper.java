package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.Country;
import ai.yarmook.shipperfinder.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
