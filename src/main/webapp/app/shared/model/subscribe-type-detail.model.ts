import { type ISubscribeType } from '@/shared/model/subscribe-type.model';

export interface ISubscribeTypeDetail {
  id?: number;
  price?: number | null;
  maxTrip?: number | null;
  maxItems?: number | null;
  maxRequest?: number | null;
  maxNumberView?: number | null;
  subscribeType?: ISubscribeType | null;
}

export class SubscribeTypeDetail implements ISubscribeTypeDetail {
  constructor(
    public id?: number,
    public price?: number | null,
    public maxTrip?: number | null,
    public maxItems?: number | null,
    public maxRequest?: number | null,
    public maxNumberView?: number | null,
    public subscribeType?: ISubscribeType | null,
  ) {}
}
