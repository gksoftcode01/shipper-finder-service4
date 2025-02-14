package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.TripMsg;
import ai.yarmook.shipperfinder.service.dto.TripMsgDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TripMsg} and its DTO {@link TripMsgDTO}.
 */
@Mapper(componentModel = "spring")
public interface TripMsgMapper extends EntityMapper<TripMsgDTO, TripMsg> {}
