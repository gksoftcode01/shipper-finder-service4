import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import TripMsgService from './trip-msg.service';
import { type ITripMsg } from '@/shared/model/trip-msg.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TripMsgDetails',
  setup() {
    const tripMsgService = inject('tripMsgService', () => new TripMsgService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const tripMsg: Ref<ITripMsg> = ref({});

    const retrieveTripMsg = async tripMsgId => {
      try {
        const res = await tripMsgService().find(tripMsgId);
        tripMsg.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tripMsgId) {
      retrieveTripMsg(route.params.tripMsgId);
    }

    return {
      alertService,
      tripMsg,

      previousState,
      t$: useI18n().t,
    };
  },
});
