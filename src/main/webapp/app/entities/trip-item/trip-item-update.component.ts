import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TripItemService from './trip-item.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ItemService from '@/entities/item/item.service';
import { type IItem } from '@/shared/model/item.model';
import UnitService from '@/entities/unit/unit.service';
import { type IUnit } from '@/shared/model/unit.model';
import TagService from '@/entities/tag/tag.service';
import { type ITag } from '@/shared/model/tag.model';
import TripService from '@/entities/trip/trip.service';
import { type ITrip } from '@/shared/model/trip.model';
import { type ITripItem, TripItem } from '@/shared/model/trip-item.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TripItemUpdate',
  setup() {
    const tripItemService = inject('tripItemService', () => new TripItemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tripItem: Ref<ITripItem> = ref(new TripItem());

    const itemService = inject('itemService', () => new ItemService());

    const items: Ref<IItem[]> = ref([]);

    const unitService = inject('unitService', () => new UnitService());

    const units: Ref<IUnit[]> = ref([]);

    const tagService = inject('tagService', () => new TagService());

    const tags: Ref<ITag[]> = ref([]);

    const tripService = inject('tripService', () => new TripService());

    const trips: Ref<ITrip[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTripItem = async tripItemId => {
      try {
        const res = await tripItemService().find(tripItemId);
        tripItem.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tripItemId) {
      retrieveTripItem(route.params.tripItemId);
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
      tripService()
        .retrieve()
        .then(res => {
          trips.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      itemPrice: {},
      maxQty: {},
      item: {},
      unit: {},
      tags: {},
      trip: {},
    };
    const v$ = useVuelidate(validationRules, tripItem as any);
    v$.value.$validate();

    return {
      tripItemService,
      alertService,
      tripItem,
      previousState,
      isSaving,
      currentLanguage,
      items,
      units,
      tags,
      trips,
      v$,
      t$,
    };
  },
  created(): void {
    this.tripItem.tags = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.tripItem.id) {
        this.tripItemService()
          .update(this.tripItem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.tripItem.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tripItemService()
          .create(this.tripItem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.tripItem.created', { param: param.id }).toString());
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
