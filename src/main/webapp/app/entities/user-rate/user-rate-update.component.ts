import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import UserRateService from './user-rate.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IUserRate, UserRate } from '@/shared/model/user-rate.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UserRateUpdate',
  setup() {
    const userRateService = inject('userRateService', () => new UserRateService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const userRate: Ref<IUserRate> = ref(new UserRate());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveUserRate = async userRateId => {
      try {
        const res = await userRateService().find(userRateId);
        res.rateDate = new Date(res.rateDate);
        userRate.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.userRateId) {
      retrieveUserRate(route.params.userRateId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      rate: {},
      note: {},
      rateDate: {},
      ratedByEncId: {},
      ratedEncId: {},
    };
    const v$ = useVuelidate(validationRules, userRate as any);
    v$.value.$validate();

    return {
      userRateService,
      alertService,
      userRate,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      ...useDateFormat({ entityRef: userRate }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.userRate.id) {
        this.userRateService()
          .update(this.userRate)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.userRate.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.userRateService()
          .create(this.userRate)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.userRate.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
