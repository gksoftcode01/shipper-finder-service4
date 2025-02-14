import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import UserRateService from './user-rate.service';
import { useDateFormat } from '@/shared/composables';
import { type IUserRate } from '@/shared/model/user-rate.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UserRateDetails',
  setup() {
    const dateFormat = useDateFormat();
    const userRateService = inject('userRateService', () => new UserRateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const userRate: Ref<IUserRate> = ref({});

    const retrieveUserRate = async userRateId => {
      try {
        const res = await userRateService().find(userRateId);
        userRate.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.userRateId) {
      retrieveUserRate(route.params.userRateId);
    }

    return {
      ...dateFormat,
      alertService,
      userRate,

      previousState,
      t$: useI18n().t,
    };
  },
});
