import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TripMsgService from './trip-msg.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ITripMsg, TripMsg } from '@/shared/model/trip-msg.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TripMsgUpdate',
  setup() {
    const tripMsgService = inject('tripMsgService', () => new TripMsgService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tripMsg: Ref<ITripMsg> = ref(new TripMsg());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTripMsg = async tripMsgId => {
      try {
        const res = await tripMsgService().find(tripMsgId);
        tripMsg.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tripMsgId) {
      retrieveTripMsg(route.params.tripMsgId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      msg: {},
      fromUserEncId: {},
      toUserEncId: {},
      tripId: {},
    };
    const v$ = useVuelidate(validationRules, tripMsg as any);
    v$.value.$validate();

    return {
      tripMsgService,
      alertService,
      tripMsg,
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
      if (this.tripMsg.id) {
        this.tripMsgService()
          .update(this.tripMsg)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.tripMsg.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tripMsgService()
          .create(this.tripMsg)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.tripMsg.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
