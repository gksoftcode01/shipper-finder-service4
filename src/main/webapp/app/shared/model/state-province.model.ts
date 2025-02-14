import { type ICountry } from '@/shared/model/country.model';

export interface IStateProvince {
  id?: number;
  name?: string | null;
  localName?: string | null;
  isoCode?: string | null;
  country?: ICountry | null;
}

export class StateProvince implements IStateProvince {
  constructor(
    public id?: number,
    public name?: string | null,
    public localName?: string | null,
    public isoCode?: string | null,
    public country?: ICountry | null,
  ) {}
}
