import { type IItemType } from '@/shared/model/item-type.model';

export interface IItem {
  id?: number;
  name?: string | null;
  nameEn?: string | null;
  nameAr?: string | null;
  nameFr?: string | null;
  nameDe?: string | null;
  nameUrdu?: string | null;
  isActive?: boolean | null;
  defaultUOM?: string | null;
  itemType?: IItemType | null;
}

export class Item implements IItem {
  constructor(
    public id?: number,
    public name?: string | null,
    public nameEn?: string | null,
    public nameAr?: string | null,
    public nameFr?: string | null,
    public nameDe?: string | null,
    public nameUrdu?: string | null,
    public isActive?: boolean | null,
    public defaultUOM?: string | null,
    public itemType?: IItemType | null,
  ) {
    this.isActive = this.isActive ?? false;
  }
}
