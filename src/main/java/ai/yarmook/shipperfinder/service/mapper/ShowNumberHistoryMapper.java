package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.ShowNumberHistory;
import ai.yarmook.shipperfinder.service.dto.ShowNumberHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShowNumberHistory} and its DTO {@link ShowNumberHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShowNumberHistoryMapper extends EntityMapper<ShowNumberHistoryDTO, ShowNumberHistory> {}
