export interface ITripMsg {
  id?: number;
  msg?: string | null;
  fromUserEncId?: string | null;
  toUserEncId?: string | null;
  tripId?: number | null;
}

export class TripMsg implements ITripMsg {
  constructor(
    public id?: number,
    public msg?: string | null,
    public fromUserEncId?: string | null,
    public toUserEncId?: string | null,
    public tripId?: number | null,
  ) {}
}
