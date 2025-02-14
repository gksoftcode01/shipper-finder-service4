package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import ai.yarmook.shipperfinder.domain.Item;
import ai.yarmook.shipperfinder.domain.Tag;
import ai.yarmook.shipperfinder.domain.Unit;
import ai.yarmook.shipperfinder.service.dto.CargoRequestDTO;
import ai.yarmook.shipperfinder.service.dto.CargoRequestItemDTO;
import ai.yarmook.shipperfinder.service.dto.ItemDTO;
import ai.yarmook.shipperfinder.service.dto.TagDTO;
import ai.yarmook.shipperfinder.service.dto.UnitDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CargoRequestItem} and its DTO {@link CargoRequestItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface CargoRequestItemMapper extends EntityMapper<CargoRequestItemDTO, CargoRequestItem> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemName")
    @Mapping(target = "unit", source = "unit", qualifiedByName = "unitName")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagNameSet")
    @Mapping(target = "cargoRequest", source = "cargoRequest", qualifiedByName = "cargoRequestId")
    CargoRequestItemDTO toDto(CargoRequestItem s);

    @Mapping(target = "removeTag", ignore = true)
    CargoRequestItem toEntity(CargoRequestItemDTO cargoRequestItemDTO);

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

    @Named("cargoRequestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CargoRequestDTO toDtoCargoRequestId(CargoRequest cargoRequest);
}
