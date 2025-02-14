import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ItemTypeService from './item-type.service';
import { type IItemType } from '@/shared/model/item-type.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ItemTypeDetails',
  setup() {
    const itemTypeService = inject('itemTypeService', () => new ItemTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const itemType: Ref<IItemType> = ref({});

    const retrieveItemType = async itemTypeId => {
      try {
        const res = await itemTypeService().find(itemTypeId);
        itemType.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.itemTypeId) {
      retrieveItemType(route.params.itemTypeId);
    }

    return {
      alertService,
      itemType,

      previousState,
      t$: useI18n().t,
    };
  },
});
