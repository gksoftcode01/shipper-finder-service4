import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import StateProvinceService from './state-province.service';
import { type IStateProvince } from '@/shared/model/state-province.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'StateProvinceDetails',
  setup() {
    const stateProvinceService = inject('stateProvinceService', () => new StateProvinceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const stateProvince: Ref<IStateProvince> = ref({});

    const retrieveStateProvince = async stateProvinceId => {
      try {
        const res = await stateProvinceService().find(stateProvinceId);
        stateProvince.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.stateProvinceId) {
      retrieveStateProvince(route.params.stateProvinceId);
    }

    return {
      alertService,
      stateProvince,

      previousState,
      t$: useI18n().t,
    };
  },
});
