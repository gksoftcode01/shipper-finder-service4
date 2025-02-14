export interface ICargoMsg {
  id?: number;
  msg?: string | null;
  fromUserEncId?: string | null;
  toUserEncId?: string | null;
  cargoRequestId?: number | null;
}

export class CargoMsg implements ICargoMsg {
  constructor(
    public id?: number,
    public msg?: string | null,
    public fromUserEncId?: string | null,
    public toUserEncId?: string | null,
    public cargoRequestId?: number | null,
  ) {}
}
