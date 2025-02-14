import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import TripService from './trip.service';
import { useDateFormat } from '@/shared/composables';
import { type ITrip } from '@/shared/model/trip.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TripDetails',
  setup() {
    const dateFormat = useDateFormat();
    const tripService = inject('tripService', () => new TripService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const trip: Ref<ITrip> = ref({});

    const retrieveTrip = async tripId => {
      try {
        const res = await tripService().find(tripId);
        trip.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tripId) {
      retrieveTrip(route.params.tripId);
    }

    return {
      ...dateFormat,
      alertService,
      trip,

      previousState,
      t$: useI18n().t,
    };
  },
});
