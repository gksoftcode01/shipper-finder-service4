import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ItemTypeService from './item-type.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IItemType, ItemType } from '@/shared/model/item-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ItemTypeUpdate',
  setup() {
    const itemTypeService = inject('itemTypeService', () => new ItemTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const itemType: Ref<IItemType> = ref(new ItemType());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      nameEn: {},
      nameAr: {},
      nameFr: {},
      nameDe: {},
      nameUrdu: {},
      isActive: {},
    };
    const v$ = useVuelidate(validationRules, itemType as any);
    v$.value.$validate();

    return {
      itemTypeService,
      alertService,
      itemType,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.itemType.id) {
        this.itemTypeService()
          .update(this.itemType)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.itemType.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.itemTypeService()
          .create(this.itemType)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.itemType.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
