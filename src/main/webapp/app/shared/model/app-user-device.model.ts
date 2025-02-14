export interface IAppUserDevice {
  id?: number;
  deviceCode?: string | null;
  notificationToken?: string | null;
  lastLogin?: Date | null;
  active?: boolean | null;
  userEncId?: string | null;
}

export class AppUserDevice implements IAppUserDevice {
  constructor(
    public id?: number,
    public deviceCode?: string | null,
    public notificationToken?: string | null,
    public lastLogin?: Date | null,
    public active?: boolean | null,
    public userEncId?: string | null,
  ) {
    this.active = this.active ?? false;
  }
}
