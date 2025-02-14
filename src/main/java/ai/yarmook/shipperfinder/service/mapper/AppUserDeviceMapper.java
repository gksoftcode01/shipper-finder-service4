package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.AppUserDevice;
import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUserDevice} and its DTO {@link AppUserDeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppUserDeviceMapper extends EntityMapper<AppUserDeviceDTO, AppUserDevice> {}
