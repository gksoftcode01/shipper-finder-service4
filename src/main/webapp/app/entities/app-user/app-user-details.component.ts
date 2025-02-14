import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import AppUserService from './app-user.service';
import { useDateFormat } from '@/shared/composables';
import { type IAppUser } from '@/shared/model/app-user.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppUserDetails',
  setup() {
    const dateFormat = useDateFormat();
    const appUserService = inject('appUserService', () => new AppUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const appUser: Ref<IAppUser> = ref({});

    const retrieveAppUser = async appUserId => {
      try {
        const res = await appUserService().find(appUserId);
        appUser.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.appUserId) {
      retrieveAppUser(route.params.appUserId);
    }

    return {
      ...dateFormat,
      alertService,
      appUser,

      previousState,
      t$: useI18n().t,
    };
  },
});
