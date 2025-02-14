package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.ItemType;
import ai.yarmook.shipperfinder.service.dto.ItemTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemType} and its DTO {@link ItemTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemTypeMapper extends EntityMapper<ItemTypeDTO, ItemType> {}
