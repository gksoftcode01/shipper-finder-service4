import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import UserSubscribeService from './user-subscribe.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SubscribeTypeService from '@/entities/subscribe-type/subscribe-type.service';
import { type ISubscribeType } from '@/shared/model/subscribe-type.model';
import { type IUserSubscribe, UserSubscribe } from '@/shared/model/user-subscribe.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UserSubscribeUpdate',
  setup() {
    const userSubscribeService = inject('userSubscribeService', () => new UserSubscribeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const userSubscribe: Ref<IUserSubscribe> = ref(new UserSubscribe());

    const subscribeTypeService = inject('subscribeTypeService', () => new SubscribeTypeService());

    const subscribeTypes: Ref<ISubscribeType[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveUserSubscribe = async userSubscribeId => {
      try {
        const res = await userSubscribeService().find(userSubscribeId);
        res.fromDate = new Date(res.fromDate);
        res.toDate = new Date(res.toDate);
        userSubscribe.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.userSubscribeId) {
      retrieveUserSubscribe(route.params.userSubscribeId);
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
      fromDate: {},
      toDate: {},
      isActive: {},
      subscribedUserEncId: {},
      subscribeType: {},
    };
    const v$ = useVuelidate(validationRules, userSubscribe as any);
    v$.value.$validate();

    return {
      userSubscribeService,
      alertService,
      userSubscribe,
      previousState,
      isSaving,
      currentLanguage,
      subscribeTypes,
      v$,
      ...useDateFormat({ entityRef: userSubscribe }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.userSubscribe.id) {
        this.userSubscribeService()
          .update(this.userSubscribe)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.userSubscribe.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.userSubscribeService()
          .create(this.userSubscribe)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.userSubscribe.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
