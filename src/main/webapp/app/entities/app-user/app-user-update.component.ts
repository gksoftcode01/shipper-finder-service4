import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import AppUserService from './app-user.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import LanguagesService from '@/entities/languages/languages.service';
import { type ILanguages } from '@/shared/model/languages.model';
import CountryService from '@/entities/country/country.service';
import { type ICountry } from '@/shared/model/country.model';
import { AppUser, type IAppUser } from '@/shared/model/app-user.model';
import { GenderType } from '@/shared/model/enumerations/gender-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppUserUpdate',
  setup() {
    const appUserService = inject('appUserService', () => new AppUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const appUser: Ref<IAppUser> = ref(new AppUser());

    const languagesService = inject('languagesService', () => new LanguagesService());

    const languages: Ref<ILanguages[]> = ref([]);

    const countryService = inject('countryService', () => new CountryService());

    const countries: Ref<ICountry[]> = ref([]);
    const genderTypeValues: Ref<string[]> = ref(Object.keys(GenderType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAppUser = async appUserId => {
      try {
        const res = await appUserService().find(appUserId);
        res.birthDate = new Date(res.birthDate);
        res.registerDate = new Date(res.registerDate);
        appUser.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.appUserId) {
      retrieveAppUser(route.params.appUserId);
    }

    const initRelationships = () => {
      languagesService()
        .retrieve()
        .then(res => {
          languages.value = res.data;
        });
      countryService()
        .retrieve()
        .then(res => {
          countries.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      birthDate: {},
      gender: {},
      registerDate: {},
      phoneNumber: {},
      mobileNumber: {},
      fullName: {},
      isVerified: {},
      userId: {},
      firstName: {},
      lastName: {},
      encId: {},
      preferdLanguage: {},
      location: {},
    };
    const v$ = useVuelidate(validationRules, appUser as any);
    v$.value.$validate();

    return {
      appUserService,
      alertService,
      appUser,
      previousState,
      genderTypeValues,
      isSaving,
      currentLanguage,
      languages,
      countries,
      v$,
      ...useDateFormat({ entityRef: appUser }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.appUser.id) {
        this.appUserService()
          .update(this.appUser)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.appUser.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.appUserService()
          .create(this.appUser)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.appUser.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
