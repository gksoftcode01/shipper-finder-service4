package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.SubscribeType;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscribeType} and its DTO {@link SubscribeTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscribeTypeMapper extends EntityMapper<SubscribeTypeDTO, SubscribeType> {}
