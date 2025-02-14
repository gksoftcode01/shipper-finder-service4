import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CargoRequestItemService from './cargo-request-item.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ItemService from '@/entities/item/item.service';
import { type IItem } from '@/shared/model/item.model';
import UnitService from '@/entities/unit/unit.service';
import { type IUnit } from '@/shared/model/unit.model';
import TagService from '@/entities/tag/tag.service';
import { type ITag } from '@/shared/model/tag.model';
import CargoRequestService from '@/entities/cargo-request/cargo-request.service';
import { type ICargoRequest } from '@/shared/model/cargo-request.model';
import { CargoRequestItem, type ICargoRequestItem } from '@/shared/model/cargo-request-item.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CargoRequestItemUpdate',
  setup() {
    const cargoRequestItemService = inject('cargoRequestItemService', () => new CargoRequestItemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const cargoRequestItem: Ref<ICargoRequestItem> = ref(new CargoRequestItem());

    const itemService = inject('itemService', () => new ItemService());

    const items: Ref<IItem[]> = ref([]);

    const unitService = inject('unitService', () => new UnitService());

    const units: Ref<IUnit[]> = ref([]);

    const tagService = inject('tagService', () => new TagService());

    const tags: Ref<ITag[]> = ref([]);

    const cargoRequestService = inject('cargoRequestService', () => new CargoRequestService());

    const cargoRequests: Ref<ICargoRequest[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      itemService()
        .retrieve()
        .then(res => {
          items.value = res.data;
        });
      unitService()
        .retrieve()
        .then(res => {
          units.value = res.data;
        });
      tagService()
        .retrieve()
        .then(res => {
          tags.value = res.data;
        });
      cargoRequestService()
        .retrieve()
        .then(res => {
          cargoRequests.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      maxQty: {},
      photoUrl: {},
      item: {},
      unit: {},
      tags: {},
      cargoRequest: {},
    };
    const v$ = useVuelidate(validationRules, cargoRequestItem as any);
    v$.value.$validate();

    return {
      cargoRequestItemService,
      alertService,
      cargoRequestItem,
      previousState,
      isSaving,
      currentLanguage,
      items,
      units,
      tags,
      cargoRequests,
      v$,
      t$,
    };
  },
  created(): void {
    this.cargoRequestItem.tags = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.cargoRequestItem.id) {
        this.cargoRequestItemService()
          .update(this.cargoRequestItem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.cargoRequestItem.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.cargoRequestItemService()
          .create(this.cargoRequestItem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.cargoRequestItem.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option, pkField = 'id'): any {
      if (selectedVals) {
        return selectedVals.find(value => option[pkField] === value[pkField]) ?? option;
      }
      return option;
    },
  },
});
