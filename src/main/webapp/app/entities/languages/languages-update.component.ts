import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import LanguagesService from './languages.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ILanguages, Languages } from '@/shared/model/languages.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LanguagesUpdate',
  setup() {
    const languagesService = inject('languagesService', () => new LanguagesService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const languages: Ref<ILanguages> = ref(new Languages());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {},
    };
    const v$ = useVuelidate(validationRules, languages as any);
    v$.value.$validate();

    return {
      languagesService,
      alertService,
      languages,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.languages.id) {
        this.languagesService()
          .update(this.languages)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.languages.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.languagesService()
          .create(this.languages)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.languages.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
