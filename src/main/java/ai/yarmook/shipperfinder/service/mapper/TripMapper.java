package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.Country;
import ai.yarmook.shipperfinder.domain.StateProvince;
import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.service.dto.CountryDTO;
import ai.yarmook.shipperfinder.service.dto.StateProvinceDTO;
import ai.yarmook.shipperfinder.service.dto.TripDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trip} and its DTO {@link TripDTO}.
 */
@Mapper(componentModel = "spring")
public interface TripMapper extends EntityMapper<TripDTO, Trip> {
    @Mapping(target = "fromCountry", source = "fromCountry", qualifiedByName = "countryName")
    @Mapping(target = "toCountry", source = "toCountry", qualifiedByName = "countryName")
    @Mapping(target = "fromState", source = "fromState", qualifiedByName = "stateProvinceName")
    @Mapping(target = "toState", source = "toState", qualifiedByName = "stateProvinceName")
    TripDTO toDto(Trip s);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);

    @Named("stateProvinceName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StateProvinceDTO toDtoStateProvinceName(StateProvince stateProvince);
}
