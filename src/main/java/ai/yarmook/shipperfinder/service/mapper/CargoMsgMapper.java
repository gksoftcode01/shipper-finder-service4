package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.CargoMsg;
import ai.yarmook.shipperfinder.service.dto.CargoMsgDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CargoMsg} and its DTO {@link CargoMsgDTO}.
 */
@Mapper(componentModel = "spring")
public interface CargoMsgMapper extends EntityMapper<CargoMsgDTO, CargoMsg> {}
