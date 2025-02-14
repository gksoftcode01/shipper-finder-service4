import { type ICountry } from '@/shared/model/country.model';
import { type IStateProvince } from '@/shared/model/state-province.model';

import { type CargoRequestStatus } from '@/shared/model/enumerations/cargo-request-status.model';
export interface ICargoRequest {
  id?: number;
  createDate?: Date | null;
  validUntil?: Date | null;
  status?: keyof typeof CargoRequestStatus | null;
  isNegotiable?: boolean | null;
  budget?: number | null;
  createdByEncId?: string | null;
  takenByEncId?: string | null;
  encId?: string | null;
  fromCountry?: ICountry | null;
  toCountry?: ICountry | null;
  fromState?: IStateProvince | null;
  toState?: IStateProvince | null;
}

export class CargoRequest implements ICargoRequest {
  constructor(
    public id?: number,
    public createDate?: Date | null,
    public validUntil?: Date | null,
    public status?: keyof typeof CargoRequestStatus | null,
    public isNegotiable?: boolean | null,
    public budget?: number | null,
    public createdByEncId?: string | null,
    public takenByEncId?: string | null,
    public encId?: string | null,
    public fromCountry?: ICountry | null,
    public toCountry?: ICountry | null,
    public fromState?: IStateProvince | null,
    public toState?: IStateProvince | null,
  ) {
    this.isNegotiable = this.isNegotiable ?? false;
  }
}
