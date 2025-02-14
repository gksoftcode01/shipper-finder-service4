import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CargoMsgService from './cargo-msg.service';
import { type ICargoMsg } from '@/shared/model/cargo-msg.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CargoMsgDetails',
  setup() {
    const cargoMsgService = inject('cargoMsgService', () => new CargoMsgService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const cargoMsg: Ref<ICargoMsg> = ref({});

    const retrieveCargoMsg = async cargoMsgId => {
      try {
        const res = await cargoMsgService().find(cargoMsgId);
        cargoMsg.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cargoMsgId) {
      retrieveCargoMsg(route.params.cargoMsgId);
    }

    return {
      alertService,
      cargoMsg,

      previousState,
      t$: useI18n().t,
    };
  },
});
