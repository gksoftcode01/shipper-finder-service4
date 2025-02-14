import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ItemService from './item.service';
import { type IItem } from '@/shared/model/item.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ItemDetails',
  setup() {
    const itemService = inject('itemService', () => new ItemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const item: Ref<IItem> = ref({});

    const retrieveItem = async itemId => {
      try {
        const res = await itemService().find(itemId);
        item.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.itemId) {
      retrieveItem(route.params.itemId);
    }

    return {
      alertService,
      item,

      previousState,
      t$: useI18n().t,
    };
  },
});
