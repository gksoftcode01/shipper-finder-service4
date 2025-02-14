package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.AppUser;
import ai.yarmook.shipperfinder.domain.Country;
import ai.yarmook.shipperfinder.domain.Languages;
import ai.yarmook.shipperfinder.service.dto.AppUserDTO;
import ai.yarmook.shipperfinder.service.dto.CountryDTO;
import ai.yarmook.shipperfinder.service.dto.LanguagesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "preferdLanguage", source = "preferdLanguage", qualifiedByName = "languagesName")
    @Mapping(target = "location", source = "location", qualifiedByName = "countryName")
    AppUserDTO toDto(AppUser s);

    @Named("languagesName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LanguagesDTO toDtoLanguagesName(Languages languages);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);
}
