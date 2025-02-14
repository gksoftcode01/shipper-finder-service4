import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CargoMsgService from './cargo-msg.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { CargoMsg, type ICargoMsg } from '@/shared/model/cargo-msg.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CargoMsgUpdate',
  setup() {
    const cargoMsgService = inject('cargoMsgService', () => new CargoMsgService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const cargoMsg: Ref<ICargoMsg> = ref(new CargoMsg());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCargoMsg = async cargoMsgId => {
      try {
        const res = await cargoMsgService().find(cargoMsgId);
        cargoMsg.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cargoMsgId) {
      retrieveCargoMsg(route.params.cargoMsgId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      msg: {},
      fromUserEncId: {},
      toUserEncId: {},
      cargoRequestId: {},
    };
    const v$ = useVuelidate(validationRules, cargoMsg as any);
    v$.value.$validate();

    return {
      cargoMsgService,
      alertService,
      cargoMsg,
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
      if (this.cargoMsg.id) {
        this.cargoMsgService()
          .update(this.cargoMsg)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.cargoMsg.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.cargoMsgService()
          .create(this.cargoMsg)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.cargoMsg.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
