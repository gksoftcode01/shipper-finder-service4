import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import UserSubscribeService from './user-subscribe.service';
import { useDateFormat } from '@/shared/composables';
import { type IUserSubscribe } from '@/shared/model/user-subscribe.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UserSubscribeDetails',
  setup() {
    const dateFormat = useDateFormat();
    const userSubscribeService = inject('userSubscribeService', () => new UserSubscribeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const userSubscribe: Ref<IUserSubscribe> = ref({});

    const retrieveUserSubscribe = async userSubscribeId => {
      try {
        const res = await userSubscribeService().find(userSubscribeId);
        userSubscribe.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.userSubscribeId) {
      retrieveUserSubscribe(route.params.userSubscribeId);
    }

    return {
      ...dateFormat,
      alertService,
      userSubscribe,

      previousState,
      t$: useI18n().t,
    };
  },
});
