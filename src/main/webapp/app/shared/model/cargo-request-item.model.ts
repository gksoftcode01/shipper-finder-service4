import { type IItem } from '@/shared/model/item.model';
import { type IUnit } from '@/shared/model/unit.model';
import { type ITag } from '@/shared/model/tag.model';
import { type ICargoRequest } from '@/shared/model/cargo-request.model';

export interface ICargoRequestItem {
  id?: number;
  maxQty?: number | null;
  photoUrl?: string | null;
  item?: IItem | null;
  unit?: IUnit | null;
  tags?: ITag[] | null;
  cargoRequest?: ICargoRequest | null;
}

export class CargoRequestItem implements ICargoRequestItem {
  constructor(
    public id?: number,
    public maxQty?: number | null,
    public photoUrl?: string | null,
    public item?: IItem | null,
    public unit?: IUnit | null,
    public tags?: ITag[] | null,
    public cargoRequest?: ICargoRequest | null,
  ) {}
}
