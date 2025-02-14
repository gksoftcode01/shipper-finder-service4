package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import ai.yarmook.shipperfinder.domain.Tag;
import ai.yarmook.shipperfinder.domain.TripItem;
import ai.yarmook.shipperfinder.service.dto.CargoRequestItemDTO;
import ai.yarmook.shipperfinder.service.dto.TagDTO;
import ai.yarmook.shipperfinder.service.dto.TripItemDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "tripItems", source = "tripItems", qualifiedByName = "tripItemIdSet")
    @Mapping(target = "cargoRequestItems", source = "cargoRequestItems", qualifiedByName = "cargoRequestItemIdSet")
    TagDTO toDto(Tag s);

    @Mapping(target = "tripItems", ignore = true)
    @Mapping(target = "removeTripItem", ignore = true)
    @Mapping(target = "cargoRequestItems", ignore = true)
    @Mapping(target = "removeCargoRequestItem", ignore = true)
    Tag toEntity(TagDTO tagDTO);

    @Named("tripItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TripItemDTO toDtoTripItemId(TripItem tripItem);

    @Named("tripItemIdSet")
    default Set<TripItemDTO> toDtoTripItemIdSet(Set<TripItem> tripItem) {
        return tripItem.stream().map(this::toDtoTripItemId).collect(Collectors.toSet());
    }

    @Named("cargoRequestItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CargoRequestItemDTO toDtoCargoRequestItemId(CargoRequestItem cargoRequestItem);

    @Named("cargoRequestItemIdSet")
    default Set<CargoRequestItemDTO> toDtoCargoRequestItemIdSet(Set<CargoRequestItem> cargoRequestItem) {
        return cargoRequestItem.stream().map(this::toDtoCargoRequestItemId).collect(Collectors.toSet());
    }
}
