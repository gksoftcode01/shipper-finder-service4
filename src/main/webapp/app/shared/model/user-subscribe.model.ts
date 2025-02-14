import { type ISubscribeType } from '@/shared/model/subscribe-type.model';

export interface IUserSubscribe {
  id?: number;
  fromDate?: Date | null;
  toDate?: Date | null;
  isActive?: boolean | null;
  subscribedUserEncId?: string | null;
  subscribeType?: ISubscribeType | null;
}

export class UserSubscribe implements IUserSubscribe {
  constructor(
    public id?: number,
    public fromDate?: Date | null,
    public toDate?: Date | null,
    public isActive?: boolean | null,
    public subscribedUserEncId?: string | null,
    public subscribeType?: ISubscribeType | null,
  ) {
    this.isActive = this.isActive ?? false;
  }
}
