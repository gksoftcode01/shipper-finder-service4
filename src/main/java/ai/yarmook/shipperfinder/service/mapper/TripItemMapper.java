package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.Item;
import ai.yarmook.shipperfinder.domain.Tag;
import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.domain.TripItem;
import ai.yarmook.shipperfinder.domain.Unit;
import ai.yarmook.shipperfinder.service.dto.ItemDTO;
import ai.yarmook.shipperfinder.service.dto.TagDTO;
import ai.yarmook.shipperfinder.service.dto.TripDTO;
import ai.yarmook.shipperfinder.service.dto.TripItemDTO;
import ai.yarmook.shipperfinder.service.dto.UnitDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TripItem} and its DTO {@link TripItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface TripItemMapper extends EntityMapper<TripItemDTO, TripItem> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemName")
    @Mapping(target = "unit", source = "unit", qualifiedByName = "unitName")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagNameSet")
    @Mapping(target = "trip", source = "trip", qualifiedByName = "tripId")
    TripItemDTO toDto(TripItem s);

    @Mapping(target = "removeTag", ignore = true)
    TripItem toEntity(TripItemDTO tripItemDTO);

    @Named("itemName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ItemDTO toDtoItemName(Item item);

    @Named("unitName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UnitDTO toDtoUnitName(Unit unit);

    @Named("tagName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TagDTO toDtoTagName(Tag tag);

    @Named("tagNameSet")
    default Set<TagDTO> toDtoTagNameSet(Set<Tag> tag) {
        return tag.stream().map(this::toDtoTagName).collect(Collectors.toSet());
    }

    @Named("tripId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TripDTO toDtoTripId(Trip trip);
}
