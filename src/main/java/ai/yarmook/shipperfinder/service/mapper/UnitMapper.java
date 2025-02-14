package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.Unit;
import ai.yarmook.shipperfinder.service.dto.UnitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Unit} and its DTO {@link UnitDTO}.
 */
@Mapper(componentModel = "spring")
public interface UnitMapper extends EntityMapper<UnitDTO, Unit> {}
