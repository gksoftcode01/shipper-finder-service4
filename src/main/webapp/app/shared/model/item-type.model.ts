export interface IItemType {
  id?: number;
  name?: string;
  nameEn?: string | null;
  nameAr?: string | null;
  nameFr?: string | null;
  nameDe?: string | null;
  nameUrdu?: string | null;
  isActive?: boolean | null;
}

export class ItemType implements IItemType {
  constructor(
    public id?: number,
    public name?: string,
    public nameEn?: string | null,
    public nameAr?: string | null,
    public nameFr?: string | null,
    public nameDe?: string | null,
    public nameUrdu?: string | null,
    public isActive?: boolean | null,
  ) {
    this.isActive = this.isActive ?? false;
  }
}
