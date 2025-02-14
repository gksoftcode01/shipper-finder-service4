import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ItemService from './item.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ItemTypeService from '@/entities/item-type/item-type.service';
import { type IItemType } from '@/shared/model/item-type.model';
import { type IItem, Item } from '@/shared/model/item.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ItemUpdate',
  setup() {
    const itemService = inject('itemService', () => new ItemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const item: Ref<IItem> = ref(new Item());

    const itemTypeService = inject('itemTypeService', () => new ItemTypeService());

    const itemTypes: Ref<IItemType[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      itemTypeService()
        .retrieve()
        .then(res => {
          itemTypes.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {},
      nameEn: {},
      nameAr: {},
      nameFr: {},
      nameDe: {},
      nameUrdu: {},
      isActive: {},
      defaultUOM: {},
      itemType: {},
    };
    const v$ = useVuelidate(validationRules, item as any);
    v$.value.$validate();

    return {
      itemService,
      alertService,
      item,
      previousState,
      isSaving,
      currentLanguage,
      itemTypes,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.item.id) {
        this.itemService()
          .update(this.item)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.item.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.itemService()
          .create(this.item)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.item.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
