import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import LanguagesService from './languages.service';
import { type ILanguages } from '@/shared/model/languages.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LanguagesDetails',
  setup() {
    const languagesService = inject('languagesService', () => new LanguagesService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const languages: Ref<ILanguages> = ref({});

    const retrieveLanguages = async languagesId => {
      try {
        const res = await languagesService().find(languagesId);
        languages.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.languagesId) {
      retrieveLanguages(route.params.languagesId);
    }

    return {
      alertService,
      languages,

      previousState,
      t$: useI18n().t,
    };
  },
});
