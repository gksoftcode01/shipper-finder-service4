import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SubscribeTypeDetailService from './subscribe-type-detail.service';
import { type ISubscribeTypeDetail } from '@/shared/model/subscribe-type-detail.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscribeTypeDetailDetails',
  setup() {
    const subscribeTypeDetailService = inject('subscribeTypeDetailService', () => new SubscribeTypeDetailService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const subscribeTypeDetail: Ref<ISubscribeTypeDetail> = ref({});

    const retrieveSubscribeTypeDetail = async subscribeTypeDetailId => {
      try {
        const res = await subscribeTypeDetailService().find(subscribeTypeDetailId);
        subscribeTypeDetail.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscribeTypeDetailId) {
      retrieveSubscribeTypeDetail(route.params.subscribeTypeDetailId);
    }

    return {
      alertService,
      subscribeTypeDetail,

      previousState,
      t$: useI18n().t,
    };
  },
});
