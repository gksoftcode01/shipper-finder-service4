import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import TripItemService from './trip-item.service';
import { type ITripItem } from '@/shared/model/trip-item.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TripItemDetails',
  setup() {
    const tripItemService = inject('tripItemService', () => new TripItemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const tripItem: Ref<ITripItem> = ref({});

    const retrieveTripItem = async tripItemId => {
      try {
        const res = await tripItemService().find(tripItemId);
        tripItem.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tripItemId) {
      retrieveTripItem(route.params.tripItemId);
    }

    return {
      alertService,
      tripItem,

      previousState,
      t$: useI18n().t,
    };
  },
});
