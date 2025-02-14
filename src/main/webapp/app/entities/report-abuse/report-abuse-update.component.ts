import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ReportAbuseService from './report-abuse.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IReportAbuse, ReportAbuse } from '@/shared/model/report-abuse.model';
import { ReportStatus } from '@/shared/model/enumerations/report-status.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReportAbuseUpdate',
  setup() {
    const reportAbuseService = inject('reportAbuseService', () => new ReportAbuseService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const reportAbuse: Ref<IReportAbuse> = ref(new ReportAbuse());
    const reportStatusValues: Ref<string[]> = ref(Object.keys(ReportStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveReportAbuse = async reportAbuseId => {
      try {
        const res = await reportAbuseService().find(reportAbuseId);
        res.reportDate = new Date(res.reportDate);
        reportAbuse.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.reportAbuseId) {
      retrieveReportAbuse(route.params.reportAbuseId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      reportByEncId: {},
      reportedAgainstEncId: {},
      reportDate: {},
      reportData: {},
      reportStatus: {},
    };
    const v$ = useVuelidate(validationRules, reportAbuse as any);
    v$.value.$validate();

    return {
      reportAbuseService,
      alertService,
      reportAbuse,
      previousState,
      reportStatusValues,
      isSaving,
      currentLanguage,
      v$,
      ...useDateFormat({ entityRef: reportAbuse }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.reportAbuse.id) {
        this.reportAbuseService()
          .update(this.reportAbuse)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.reportAbuse.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.reportAbuseService()
          .create(this.reportAbuse)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.reportAbuse.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
