import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SubscribeTypeDetailService from './subscribe-type-detail.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SubscribeTypeService from '@/entities/subscribe-type/subscribe-type.service';
import { type ISubscribeType } from '@/shared/model/subscribe-type.model';
import { type ISubscribeTypeDetail, SubscribeTypeDetail } from '@/shared/model/subscribe-type-detail.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscribeTypeDetailUpdate',
  setup() {
    const subscribeTypeDetailService = inject('subscribeTypeDetailService', () => new SubscribeTypeDetailService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const subscribeTypeDetail: Ref<ISubscribeTypeDetail> = ref(new SubscribeTypeDetail());

    const subscribeTypeService = inject('subscribeTypeService', () => new SubscribeTypeService());

    const subscribeTypes: Ref<ISubscribeType[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSubscribeTypeDetail = async subscribeTypeDetailId => {
      try {
        const res = await subscribeTypeDetailService().find(subscribeTypeDetailId);
        subscribeTypeDetail.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscribeTypeDetailId) {
      retrieveSubscribeTypeDetail(route.params.subscribeTypeDetailId);
    }

    const initRelationships = () => {
      subscribeTypeService()
        .retrieve()
        .then(res => {
          subscribeTypes.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      price: {},
      maxTrip: {},
      maxItems: {},
      maxRequest: {},
      maxNumberView: {},
      subscribeType: {},
    };
    const v$ = useVuelidate(validationRules, subscribeTypeDetail as any);
    v$.value.$validate();

    return {
      subscribeTypeDetailService,
      alertService,
      subscribeTypeDetail,
      previousState,
      isSaving,
      currentLanguage,
      subscribeTypes,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.subscribeTypeDetail.id) {
        this.subscribeTypeDetailService()
          .update(this.subscribeTypeDetail)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.subscribeTypeDetail.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.subscribeTypeDetailService()
          .create(this.subscribeTypeDetail)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.subscribeTypeDetail.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
