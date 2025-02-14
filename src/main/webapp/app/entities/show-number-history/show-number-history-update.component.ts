import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ShowNumberHistoryService from './show-number-history.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IShowNumberHistory, ShowNumberHistory } from '@/shared/model/show-number-history.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ShowNumberHistoryUpdate',
  setup() {
    const showNumberHistoryService = inject('showNumberHistoryService', () => new ShowNumberHistoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const showNumberHistory: Ref<IShowNumberHistory> = ref(new ShowNumberHistory());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveShowNumberHistory = async showNumberHistoryId => {
      try {
        const res = await showNumberHistoryService().find(showNumberHistoryId);
        res.createdDate = new Date(res.createdDate);
        showNumberHistory.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.showNumberHistoryId) {
      retrieveShowNumberHistory(route.params.showNumberHistoryId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      createdDate: {},
      actionByEncId: {},
      entityType: {},
      entityEncId: {},
    };
    const v$ = useVuelidate(validationRules, showNumberHistory as any);
    v$.value.$validate();

    return {
      showNumberHistoryService,
      alertService,
      showNumberHistory,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      ...useDateFormat({ entityRef: showNumberHistory }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.showNumberHistory.id) {
        this.showNumberHistoryService()
          .update(this.showNumberHistory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.showNumberHistory.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.showNumberHistoryService()
          .create(this.showNumberHistory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.showNumberHistory.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
