package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.UserRate;
import ai.yarmook.shipperfinder.service.dto.UserRateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserRate} and its DTO {@link UserRateDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserRateMapper extends EntityMapper<UserRateDTO, UserRate> {}
