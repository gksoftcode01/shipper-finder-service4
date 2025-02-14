import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CargoRequestItemService from './cargo-request-item.service';
import { type ICargoRequestItem } from '@/shared/model/cargo-request-item.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CargoRequestItemDetails',
  setup() {
    const cargoRequestItemService = inject('cargoRequestItemService', () => new CargoRequestItemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const cargoRequestItem: Ref<ICargoRequestItem> = ref({});

    const retrieveCargoRequestItem = async cargoRequestItemId => {
      try {
        const res = await cargoRequestItemService().find(cargoRequestItemId);
        cargoRequestItem.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cargoRequestItemId) {
      retrieveCargoRequestItem(route.params.cargoRequestItemId);
    }

    return {
      alertService,
      cargoRequestItem,

      previousState,
      t$: useI18n().t,
    };
  },
});
