package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.Country;
import ai.yarmook.shipperfinder.domain.StateProvince;
import ai.yarmook.shipperfinder.service.dto.CountryDTO;
import ai.yarmook.shipperfinder.service.dto.StateProvinceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StateProvince} and its DTO {@link StateProvinceDTO}.
 */
@Mapper(componentModel = "spring")
public interface StateProvinceMapper extends EntityMapper<StateProvinceDTO, StateProvince> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryName")
    StateProvinceDTO toDto(StateProvince s);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);
}
