import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ReportAbuseService from './report-abuse.service';
import { useDateFormat } from '@/shared/composables';
import { type IReportAbuse } from '@/shared/model/report-abuse.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReportAbuseDetails',
  setup() {
    const dateFormat = useDateFormat();
    const reportAbuseService = inject('reportAbuseService', () => new ReportAbuseService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const reportAbuse: Ref<IReportAbuse> = ref({});

    const retrieveReportAbuse = async reportAbuseId => {
      try {
        const res = await reportAbuseService().find(reportAbuseId);
        reportAbuse.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.reportAbuseId) {
      retrieveReportAbuse(route.params.reportAbuseId);
    }

    return {
      ...dateFormat,
      alertService,
      reportAbuse,

      previousState,
      t$: useI18n().t,
    };
  },
});
