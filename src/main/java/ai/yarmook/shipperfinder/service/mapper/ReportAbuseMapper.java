package ai.yarmook.shipperfinder.service.mapper;

import ai.yarmook.shipperfinder.domain.ReportAbuse;
import ai.yarmook.shipperfinder.service.dto.ReportAbuseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportAbuse} and its DTO {@link ReportAbuseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportAbuseMapper extends EntityMapper<ReportAbuseDTO, ReportAbuse> {}
