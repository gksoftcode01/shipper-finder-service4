package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.SubscribeType;
import ai.yarmook.shipperfinder.domain.SubscribeTypeDetail;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDTO;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscribeTypeDetail} and its DTO {@link SubscribeTypeDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscribeTypeDetailMapper extends EntityMapper<SubscribeTypeDetailDTO, SubscribeTypeDetail> {
    @Mapping(target = "subscribeType", source = "subscribeType", qualifiedByName = "subscribeTypeNameEn")
    SubscribeTypeDetailDTO toDto(SubscribeTypeDetail s);

    @Named("subscribeTypeNameEn")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nameEn", source = "nameEn")
    SubscribeTypeDTO toDtoSubscribeTypeNameEn(SubscribeType subscribeType);
}
