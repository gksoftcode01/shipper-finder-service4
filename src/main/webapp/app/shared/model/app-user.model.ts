import { type ILanguages } from '@/shared/model/languages.model';
import { type ICountry } from '@/shared/model/country.model';

import { type GenderType } from '@/shared/model/enumerations/gender-type.model';
export interface IAppUser {
  id?: number;
  birthDate?: Date | null;
  gender?: keyof typeof GenderType | null;
  registerDate?: Date | null;
  phoneNumber?: string | null;
  mobileNumber?: string | null;
  fullName?: string | null;
  isVerified?: boolean | null;
  userId?: number | null;
  firstName?: string | null;
  lastName?: string | null;
  encId?: string | null;
  preferdLanguage?: ILanguages | null;
  location?: ICountry | null;
}

export class AppUser implements IAppUser {
  constructor(
    public id?: number,
    public birthDate?: Date | null,
    public gender?: keyof typeof GenderType | null,
    public registerDate?: Date | null,
    public phoneNumber?: string | null,
    public mobileNumber?: string | null,
    public fullName?: string | null,
    public isVerified?: boolean | null,
    public userId?: number | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public encId?: string | null,
    public preferdLanguage?: ILanguages | null,
    public location?: ICountry | null,
  ) {
    this.isVerified = this.isVerified ?? false;
  }
}
