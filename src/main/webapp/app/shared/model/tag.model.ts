import { type ITripItem } from '@/shared/model/trip-item.model';
import { type ICargoRequestItem } from '@/shared/model/cargo-request-item.model';

export interface ITag {
  id?: number;
  name?: string | null;
  nameEn?: string | null;
  nameAr?: string | null;
  nameFr?: string | null;
  nameDe?: string | null;
  nameUrdu?: string | null;
  iconUrl?: string | null;
  tripItems?: ITripItem[] | null;
  cargoRequestItems?: ICargoRequestItem[] | null;
}

export class Tag implements ITag {
  constructor(
    public id?: number,
    public name?: string | null,
    public nameEn?: string | null,
    public nameAr?: string | null,
    public nameFr?: string | null,
    public nameDe?: string | null,
    public nameUrdu?: string | null,
    public iconUrl?: string | null,
    public tripItems?: ITripItem[] | null,
    public cargoRequestItems?: ICargoRequestItem[] | null,
  ) {}
}
