package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.SubscribeType;
import ai.yarmook.shipperfinder.domain.UserSubscribe;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDTO;
import ai.yarmook.shipperfinder.service.dto.UserSubscribeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserSubscribe} and its DTO {@link UserSubscribeDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserSubscribeMapper extends EntityMapper<UserSubscribeDTO, UserSubscribe> {
    @Mapping(target = "subscribeType", source = "subscribeType", qualifiedByName = "subscribeTypeType")
    UserSubscribeDTO toDto(UserSubscribe s);

    @Named("subscribeTypeType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    SubscribeTypeDTO toDtoSubscribeTypeType(SubscribeType subscribeType);
}
