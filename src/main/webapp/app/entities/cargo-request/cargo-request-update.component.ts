import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CargoRequestService from './cargo-request.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import CountryService from '@/entities/country/country.service';
import { type ICountry } from '@/shared/model/country.model';
import StateProvinceService from '@/entities/state-province/state-province.service';
import { type IStateProvince } from '@/shared/model/state-province.model';
import { CargoRequest, type ICargoRequest } from '@/shared/model/cargo-request.model';
import { CargoRequestStatus } from '@/shared/model/enumerations/cargo-request-status.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CargoRequestUpdate',
  setup() {
    const cargoRequestService = inject('cargoRequestService', () => new CargoRequestService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const cargoRequest: Ref<ICargoRequest> = ref(new CargoRequest());

    const countryService = inject('countryService', () => new CountryService());

    const countries: Ref<ICountry[]> = ref([]);

    const stateProvinceService = inject('stateProvinceService', () => new StateProvinceService());

    const stateProvinces: Ref<IStateProvince[]> = ref([]);
    const cargoRequestStatusValues: Ref<string[]> = ref(Object.keys(CargoRequestStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCargoRequest = async cargoRequestId => {
      try {
        const res = await cargoRequestService().find(cargoRequestId);
        res.createDate = new Date(res.createDate);
        res.validUntil = new Date(res.validUntil);
        cargoRequest.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cargoRequestId) {
      retrieveCargoRequest(route.params.cargoRequestId);
    }

    const initRelationships = () => {
      countryService()
        .retrieve()
        .then(res => {
          countries.value = res.data;
        });
      stateProvinceService()
        .retrieve()
        .then(res => {
          stateProvinces.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      createDate: {},
      validUntil: {},
      status: {},
      isNegotiable: {},
      budget: {},
      createdByEncId: {},
      takenByEncId: {},
      encId: {},
      items: {},
      fromCountry: {},
      toCountry: {},
      fromState: {},
      toState: {},
    };
    const v$ = useVuelidate(validationRules, cargoRequest as any);
    v$.value.$validate();

    return {
      cargoRequestService,
      alertService,
      cargoRequest,
      previousState,
      cargoRequestStatusValues,
      isSaving,
      currentLanguage,
      countries,
      stateProvinces,
      v$,
      ...useDateFormat({ entityRef: cargoRequest }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.cargoRequest.id) {
        this.cargoRequestService()
          .update(this.cargoRequest)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.cargoRequest.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.cargoRequestService()
          .create(this.cargoRequest)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.cargoRequest.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
