import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TagService from './tag.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import TripItemService from '@/entities/trip-item/trip-item.service';
import { type ITripItem } from '@/shared/model/trip-item.model';
import CargoRequestItemService from '@/entities/cargo-request-item/cargo-request-item.service';
import { type ICargoRequestItem } from '@/shared/model/cargo-request-item.model';
import { type ITag, Tag } from '@/shared/model/tag.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TagUpdate',
  setup() {
    const tagService = inject('tagService', () => new TagService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tag: Ref<ITag> = ref(new Tag());

    const tripItemService = inject('tripItemService', () => new TripItemService());

    const tripItems: Ref<ITripItem[]> = ref([]);

    const cargoRequestItemService = inject('cargoRequestItemService', () => new CargoRequestItemService());

    const cargoRequestItems: Ref<ICargoRequestItem[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTag = async tagId => {
      try {
        const res = await tagService().find(tagId);
        tag.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tagId) {
      retrieveTag(route.params.tagId);
    }

    const initRelationships = () => {
      tripItemService()
        .retrieve()
        .then(res => {
          tripItems.value = res.data;
        });
      cargoRequestItemService()
        .retrieve()
        .then(res => {
          cargoRequestItems.value = res.data;
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
      iconUrl: {},
      tripItems: {},
      cargoRequestItems: {},
    };
    const v$ = useVuelidate(validationRules, tag as any);
    v$.value.$validate();

    return {
      tagService,
      alertService,
      tag,
      previousState,
      isSaving,
      currentLanguage,
      tripItems,
      cargoRequestItems,
      v$,
      t$,
    };
  },
  created(): void {
    this.tag.tripItems = [];
    this.tag.cargoRequestItems = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.tag.id) {
        this.tagService()
          .update(this.tag)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.tag.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tagService()
          .create(this.tag)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.tag.created', { param: param.id }).toString());
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
