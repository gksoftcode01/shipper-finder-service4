import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import StateProvinceService from './state-province.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import CountryService from '@/entities/country/country.service';
import { type ICountry } from '@/shared/model/country.model';
import { type IStateProvince, StateProvince } from '@/shared/model/state-province.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'StateProvinceUpdate',
  setup() {
    const stateProvinceService = inject('stateProvinceService', () => new StateProvinceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const stateProvince: Ref<IStateProvince> = ref(new StateProvince());

    const countryService = inject('countryService', () => new CountryService());

    const countries: Ref<ICountry[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveStateProvince = async stateProvinceId => {
      try {
        const res = await stateProvinceService().find(stateProvinceId);
        stateProvince.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.stateProvinceId) {
      retrieveStateProvince(route.params.stateProvinceId);
    }

    const initRelationships = () => {
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
      name: {},
      localName: {},
      isoCode: {},
      country: {},
    };
    const v$ = useVuelidate(validationRules, stateProvince as any);
    v$.value.$validate();

    return {
      stateProvinceService,
      alertService,
      stateProvince,
      previousState,
      isSaving,
      currentLanguage,
      countries,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.stateProvince.id) {
        this.stateProvinceService()
          .update(this.stateProvince)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.stateProvince.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.stateProvinceService()
          .create(this.stateProvince)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.stateProvince.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
