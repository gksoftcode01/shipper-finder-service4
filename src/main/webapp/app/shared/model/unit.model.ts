export interface IUnit {
  id?: number;
  name?: string | null;
  nameEn?: string | null;
  nameAr?: string | null;
  nameFr?: string | null;
  nameDe?: string | null;
  nameUrdu?: string | null;
}

export class Unit implements IUnit {
  constructor(
    public id?: number,
    public name?: string | null,
    public nameEn?: string | null,
    public nameAr?: string | null,
    public nameFr?: string | null,
    public nameDe?: string | null,
    public nameUrdu?: string | null,
  ) {}
}
