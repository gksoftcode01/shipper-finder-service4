import { type ReportStatus } from '@/shared/model/enumerations/report-status.model';
export interface IReportAbuse {
  id?: number;
  reportByEncId?: string | null;
  reportedAgainstEncId?: string | null;
  reportDate?: Date | null;
  reportData?: string | null;
  reportStatus?: keyof typeof ReportStatus | null;
}

export class ReportAbuse implements IReportAbuse {
  constructor(
    public id?: number,
    public reportByEncId?: string | null,
    public reportedAgainstEncId?: string | null,
    public reportDate?: Date | null,
    public reportData?: string | null,
    public reportStatus?: keyof typeof ReportStatus | null,
  ) {}
}
