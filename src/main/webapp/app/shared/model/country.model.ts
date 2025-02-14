export interface ICountry {
  id?: number;
  name?: string | null;
  localName?: string | null;
  iso2?: string | null;
  iso3?: string | null;
  numericCode?: string | null;
  phoneCode?: string | null;
  currency?: string | null;
  currencyName?: string | null;
  currencySymbol?: string | null;
  emoji?: string | null;
  emojiU?: string | null;
}

export class Country implements ICountry {
  constructor(
    public id?: number,
    public name?: string | null,
    public localName?: string | null,
    public iso2?: string | null,
    public iso3?: string | null,
    public numericCode?: string | null,
    public phoneCode?: string | null,
    public currency?: string | null,
    public currencyName?: string | null,
    public currencySymbol?: string | null,
    public emoji?: string | null,
    public emojiU?: string | null,
  ) {}
}
