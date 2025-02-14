import { type ICountry } from '@/shared/model/country.model';
import { type IStateProvince } from '@/shared/model/state-province.model';

import { type TripStatus } from '@/shared/model/enumerations/trip-status.model';
export interface ITrip {
  id?: number;
  tripDate?: Date | null;
  arriveDate?: Date | null;
  maxWeight?: number | null;
  notes?: string | null;
  createDate?: Date | null;
  isNegotiate?: boolean | null;
  status?: keyof typeof TripStatus | null;
  createdByEncId?: string | null;
  encId?: string | null;
  fromCountry?: ICountry | null;
  toCountry?: ICountry | null;
  fromState?: IStateProvince | null;
  toState?: IStateProvince | null;
}

export class Trip implements ITrip {
  constructor(
    public id?: number,
    public tripDate?: Date | null,
    public arriveDate?: Date | null,
    public maxWeight?: number | null,
    public notes?: string | null,
    public createDate?: Date | null,
    public isNegotiate?: boolean | null,
    public status?: keyof typeof TripStatus | null,
    public createdByEncId?: string | null,
    public encId?: string | null,
    public fromCountry?: ICountry | null,
    public toCountry?: ICountry | null,
    public fromState?: IStateProvince | null,
    public toState?: IStateProvince | null,
  ) {
    this.isNegotiate = this.isNegotiate ?? false;
  }
}
