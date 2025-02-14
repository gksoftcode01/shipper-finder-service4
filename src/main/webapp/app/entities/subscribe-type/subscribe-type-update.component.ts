import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SubscribeTypeService from './subscribe-type.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ISubscribeType, SubscribeType } from '@/shared/model/subscribe-type.model';
import { SubscribeTypeEnum } from '@/shared/model/enumerations/subscribe-type-enum.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscribeTypeUpdate',
  setup() {
    const subscribeTypeService = inject('subscribeTypeService', () => new SubscribeTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const subscribeType: Ref<ISubscribeType> = ref(new SubscribeType());
    const subscribeTypeEnumValues: Ref<string[]> = ref(Object.keys(SubscribeTypeEnum));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSubscribeType = async subscribeTypeId => {
      try {
        const res = await subscribeTypeService().find(subscribeTypeId);
        subscribeType.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscribeTypeId) {
      retrieveSubscribeType(route.params.subscribeTypeId);
    }

    const initRelationships = () => {};

    initRelationships();

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      type: {},
      nameEn: {},
      nameAr: {},
      nameFr: {},
      nameDe: {},
      nameUrdu: {},
      details: {},
      detailsEn: {},
      detailsAr: {},
      detailsFr: {},
      detailsDe: {},
      detailsUrdu: {},
      subscribeTypeDetail: {},
    };
    const v$ = useVuelidate(validationRules, subscribeType as any);
    v$.value.$validate();

    return {
      subscribeTypeService,
      alertService,
      subscribeType,
      previousState,
      subscribeTypeEnumValues,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.subscribeType.id) {
        this.subscribeTypeService()
          .update(this.subscribeType)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.subscribeType.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.subscribeTypeService()
          .create(this.subscribeType)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.subscribeType.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
