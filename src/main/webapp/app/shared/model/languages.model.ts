export interface ILanguages {
  id?: number;
  name?: string | null;
}

export class Languages implements ILanguages {
  constructor(
    public id?: number,
    public name?: string | null,
  ) {}
}
