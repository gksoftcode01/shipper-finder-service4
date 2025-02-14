package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.Item;
import ai.yarmook.shipperfinder.domain.ItemType;
import ai.yarmook.shipperfinder.service.dto.ItemDTO;
import ai.yarmook.shipperfinder.service.dto.ItemTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "itemType", source = "itemType", qualifiedByName = "itemTypeName")
    ItemDTO toDto(Item s);

    @Named("itemTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ItemTypeDTO toDtoItemTypeName(ItemType itemType);
}
