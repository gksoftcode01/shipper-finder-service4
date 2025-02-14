import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import AppUserDeviceService from './app-user-device.service';
import { useDateFormat } from '@/shared/composables';
import { type IAppUserDevice } from '@/shared/model/app-user-device.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppUserDeviceDetails',
  setup() {
    const dateFormat = useDateFormat();
    const appUserDeviceService = inject('appUserDeviceService', () => new AppUserDeviceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const appUserDevice: Ref<IAppUserDevice> = ref({});

    const retrieveAppUserDevice = async appUserDeviceId => {
      try {
        const res = await appUserDeviceService().find(appUserDeviceId);
        appUserDevice.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.appUserDeviceId) {
      retrieveAppUserDevice(route.params.appUserDeviceId);
    }

    return {
      ...dateFormat,
      alertService,
      appUserDevice,

      previousState,
      t$: useI18n().t,
    };
  },
});
