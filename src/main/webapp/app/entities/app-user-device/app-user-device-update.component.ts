import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import AppUserDeviceService from './app-user-device.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { AppUserDevice, type IAppUserDevice } from '@/shared/model/app-user-device.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppUserDeviceUpdate',
  setup() {
    const appUserDeviceService = inject('appUserDeviceService', () => new AppUserDeviceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const appUserDevice: Ref<IAppUserDevice> = ref(new AppUserDevice());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAppUserDevice = async appUserDeviceId => {
      try {
        const res = await appUserDeviceService().find(appUserDeviceId);
        res.lastLogin = new Date(res.lastLogin);
        appUserDevice.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.appUserDeviceId) {
      retrieveAppUserDevice(route.params.appUserDeviceId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      deviceCode: {},
      notificationToken: {},
      lastLogin: {},
      active: {},
      userEncId: {},
    };
    const v$ = useVuelidate(validationRules, appUserDevice as any);
    v$.value.$validate();

    return {
      appUserDeviceService,
      alertService,
      appUserDevice,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      ...useDateFormat({ entityRef: appUserDevice }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.appUserDevice.id) {
        this.appUserDeviceService()
          .update(this.appUserDevice)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('shipperfinderservice4App.appUserDevice.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.appUserDeviceService()
          .create(this.appUserDevice)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('shipperfinderservice4App.appUserDevice.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
