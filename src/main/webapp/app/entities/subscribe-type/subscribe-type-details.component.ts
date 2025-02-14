import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SubscribeTypeService from './subscribe-type.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ISubscribeType } from '@/shared/model/subscribe-type.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscribeTypeDetails',
  setup() {
    const subscribeTypeService = inject('subscribeTypeService', () => new SubscribeTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const subscribeType: Ref<ISubscribeType> = ref({});

    const retrieveSubscribeType = async subscribeTypeId => {
      try {
        const res = await subscribeTypeService().find(subscribeTypeId);
        subscribeType.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscribeTypeId) {
      retrieveSubscribeType(route.params.subscribeTypeId);
    }

    return {
      alertService,
      subscribeType,

      ...dataUtils,

      previousState,
      t$: useI18n().t,
    };
  },
});
