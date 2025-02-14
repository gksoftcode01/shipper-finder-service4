import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import UnitService from './unit.service';
import { type IUnit } from '@/shared/model/unit.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UnitDetails',
  setup() {
    const unitService = inject('unitService', () => new UnitService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const unit: Ref<IUnit> = ref({});

    const retrieveUnit = async unitId => {
      try {
        const res = await unitService().find(unitId);
        unit.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.unitId) {
      retrieveUnit(route.params.unitId);
    }

    return {
      alertService,
      unit,

      previousState,
      t$: useI18n().t,
    };
  },
});
