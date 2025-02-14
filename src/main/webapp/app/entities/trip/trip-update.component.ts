import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TripService from './trip.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import CountryService from '@/entities/country/country.service';
import { type ICountry } from '@/shared/model/country.model';
import StateProvinceService from '@/entities/state-province/state-province.service';
import { type IStateProvince } from '@/shared/model/state-province.model';
import { type ITrip, Trip } from '@/shared/model/trip.model';
import { TripStatus } from '@/shared/model/enumerations/trip-status.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TripUpdate',
  setup() {
    const tripService = inject('tripService', () => new TripService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const trip: Ref<ITrip> = ref(new Trip());

    const countryService = inject('countryService', () => new CountryService());

    const countries: Ref<ICountry[]> = ref([]);

    const stateProvinceService = inject('stateProvinceService', () => new StateProvinceService());

    const stateProvinces: Ref<IStateProvince[]> = ref([]);
    const tripStatusValues: Ref<string[]> = ref(Object.keys(TripStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTrip = async tripId => {
      try {
        const res = await tripService().find(tripId);
        res.tripDate = new Date(res.tripDate);
        res.arriveDate = new Date(res.arriveDate);
        res.createDate = new Date(res.createDate);
        trip.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tripId) {
      retrieveTrip(route.params.tripId);
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
      tripDate: {},
      arriveDate: {},
      maxWeight: {},
      notes: {},
      createDate: {},
      isNegotiate: {},
      status: {},
      createdByEncId: {},
      encId: {},
      items: {},
      fromCountry: {},
      toCountry: {},
      fromState: {},
      toState: {},
    };
    const v$ = useVuelidate(validationRules, trip as any);
    v$.value.$validate();

    return {
      tripService,
      alertService,
      trip,
      previousState,
      tripStatusValues,
      isSaving,
      currentLanguage,
      countries,
      stateProvinces,
      v$,
      ...useDateFormat({ entityRef: trip }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.trip.id) {
        this.tripService()
          .update(this.trip)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.trip.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tripService()
          .create(this.trip)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.trip.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
