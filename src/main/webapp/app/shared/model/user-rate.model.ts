export interface IUserRate {
  id?: number;
  rate?: number | null;
  note?: string | null;
  rateDate?: Date | null;
  ratedByEncId?: string | null;
  ratedEncId?: string | null;
}

export class UserRate implements IUserRate {
  constructor(
    public id?: number,
    public rate?: number | null,
    public note?: string | null,
    public rateDate?: Date | null,
    public ratedByEncId?: string | null,
    public ratedEncId?: string | null,
  ) {}
}
