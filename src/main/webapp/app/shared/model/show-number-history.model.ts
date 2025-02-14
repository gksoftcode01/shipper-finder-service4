export interface IShowNumberHistory {
  id?: number;
  createdDate?: Date | null;
  actionByEncId?: string | null;
  entityType?: number | null;
  entityEncId?: string | null;
}

export class ShowNumberHistory implements IShowNumberHistory {
  constructor(
    public id?: number,
    public createdDate?: Date | null,
    public actionByEncId?: string | null,
    public entityType?: number | null,
    public entityEncId?: string | null,
  ) {}
}
