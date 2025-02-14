import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CargoRequestService from './cargo-request.service';
import { useDateFormat } from '@/shared/composables';
import { type ICargoRequest } from '@/shared/model/cargo-request.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CargoRequestDetails',
  setup() {
    const dateFormat = useDateFormat();
    const cargoRequestService = inject('cargoRequestService', () => new CargoRequestService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const cargoRequest: Ref<ICargoRequest> = ref({});

    const retrieveCargoRequest = async cargoRequestId => {
      try {
        const res = await cargoRequestService().find(cargoRequestId);
        cargoRequest.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cargoRequestId) {
      retrieveCargoRequest(route.params.cargoRequestId);
    }

    return {
      ...dateFormat,
      alertService,
      cargoRequest,

      previousState,
      t$: useI18n().t,
    };
  },
});
