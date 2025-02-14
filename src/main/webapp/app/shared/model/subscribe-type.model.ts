import { type SubscribeTypeEnum } from '@/shared/model/enumerations/subscribe-type-enum.model';
export interface ISubscribeType {
  id?: number;
  type?: keyof typeof SubscribeTypeEnum | null;
  nameEn?: string | null;
  nameAr?: string | null;
  nameFr?: string | null;
  nameDe?: string | null;
  nameUrdu?: string | null;
  details?: string | null;
  detailsEn?: string | null;
  detailsAr?: string | null;
  detailsFr?: string | null;
  detailsDe?: string | null;
  detailsUrdu?: string | null;
}

export class SubscribeType implements ISubscribeType {
  constructor(
    public id?: number,
    public type?: keyof typeof SubscribeTypeEnum | null,
    public nameEn?: string | null,
    public nameAr?: string | null,
    public nameFr?: string | null,
    public nameDe?: string | null,
    public nameUrdu?: string | null,
    public details?: string | null,
    public detailsEn?: string | null,
    public detailsAr?: string | null,
    public detailsFr?: string | null,
    public detailsDe?: string | null,
    public detailsUrdu?: string | null,
  ) {}
}
