import { type IItem } from '@/shared/model/item.model';
import { type IUnit } from '@/shared/model/unit.model';
import { type ITag } from '@/shared/model/tag.model';
import { type ITrip } from '@/shared/model/trip.model';

export interface ITripItem {
  id?: number;
  itemPrice?: number | null;
  maxQty?: number | null;
  item?: IItem | null;
  unit?: IUnit | null;
  tags?: ITag[] | null;
  trip?: ITrip | null;
}

export class TripItem implements ITripItem {
  constructor(
    public id?: number,
    public itemPrice?: number | null,
    public maxQty?: number | null,
    public item?: IItem | null,
    public unit?: IUnit | null,
    public tags?: ITag[] | null,
    public trip?: ITrip | null,
  ) {}
}
