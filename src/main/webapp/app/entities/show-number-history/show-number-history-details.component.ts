import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ShowNumberHistoryService from './show-number-history.service';
import { useDateFormat } from '@/shared/composables';
import { type IShowNumberHistory } from '@/shared/model/show-number-history.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ShowNumberHistoryDetails',
  setup() {
    const dateFormat = useDateFormat();
    const showNumberHistoryService = inject('showNumberHistoryService', () => new ShowNumberHistoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const showNumberHistory: Ref<IShowNumberHistory> = ref({});

    const retrieveShowNumberHistory = async showNumberHistoryId => {
      try {
        const res = await showNumberHistoryService().find(showNumberHistoryId);
        showNumberHistory.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.showNumberHistoryId) {
      retrieveShowNumberHistory(route.params.showNumberHistoryId);
    }

    return {
      ...dateFormat,
      alertService,
      showNumberHistory,

      previousState,
      t$: useI18n().t,
    };
  },
});
