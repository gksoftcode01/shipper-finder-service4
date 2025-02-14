import { type Ref, defineComponent, inject, onMounted, ref, watch, watchEffect } from 'vue';
import { useI18n } from 'vue-i18n';
import { useIntersectionObserver } from '@vueuse/core';

import ItemTypeService from './item-type.service';
import { type IItemType } from '@/shared/model/item-type.model';
import useDataUtils from '@/shared/data/data-utils.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ItemType',
  setup() {
    const { t: t$ } = useI18n();
    const dataUtils = useDataUtils();
    const itemTypeService = inject('itemTypeService', () => new ItemTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const currentSearch = ref('');
    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(null);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);
    const links: Ref<any> = ref({});

    const itemTypes: Ref<IItemType[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {
      currentSearch.value = '';
      page.value = 1;
      links.value = {};
      itemTypes.value = [];
    };

    const sort = (): Array<any> => {
      const result = [`${propOrder.value},${reverse.value ? 'desc' : 'asc'}`];
      if (propOrder.value !== 'id') {
        result.push('id');
      }
      return result;
    };

    const retrieveItemTypes = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
        };
        const res = currentSearch.value
          ? await itemTypeService().search(currentSearch.value, paginationQuery)
          : await itemTypeService().retrieve(paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        links.value = dataUtils.parseLinks(res.headers?.link);
        itemTypes.value.push(...(res.data ?? []));
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      clear();
    };

    onMounted(async () => {
      await retrieveItemTypes();
    });

    const search = query => {
      if (!query) {
        return clear();
      }
      currentSearch.value = query;
      retrieveItemTypes();
    };

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IItemType) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeItemType = async () => {
      try {
        await itemTypeService().delete(removeId.value);
        const message = t$('shipperfinderservice4App.itemType.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        clear();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const changeOrder = (newOrder: string) => {
      if (propOrder.value === newOrder) {
        reverse.value = !reverse.value;
      } else {
        reverse.value = false;
      }
      propOrder.value = newOrder;
    };

    // Whenever order changes, reset the pagination
    watch([propOrder, reverse], () => {
      clear();
    });

    // Whenever the data resets or page changes, switch to the new page.
    watch([itemTypes, page], async ([data, page], [_prevData, prevPage]) => {
      if (data.length === 0 || page !== prevPage) {
        await retrieveItemTypes();
      }
    });

    const infiniteScrollEl = ref<HTMLElement>(null);
    const intersectionObserver = useIntersectionObserver(
      infiniteScrollEl,
      intersection => {
        if (intersection[0].isIntersecting && !isFetching.value) {
          page.value++;
        }
      },
      {
        threshold: 0.5,
        immediate: false,
      },
    );
    watchEffect(() => {
      if (links.value.next) {
        intersectionObserver.resume();
      } else if (intersectionObserver.isActive) {
        intersectionObserver.pause();
      }
    });

    return {
      itemTypes,
      handleSyncList,
      isFetching,
      retrieveItemTypes,
      clear,
      currentSearch,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeItemType,
      itemsPerPage,
      queryCount,
      page,
      propOrder,
      reverse,
      totalItems,
      changeOrder,
      infiniteScrollEl,
      t$,
      ...dataUtils,
    };
  },
});
