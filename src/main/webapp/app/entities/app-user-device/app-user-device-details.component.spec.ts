import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AppUserDeviceDetails from './app-user-device-details.vue';
import AppUserDeviceService from './app-user-device.service';
import AlertService from '@/shared/alert/alert.service';

type AppUserDeviceDetailsComponentType = InstanceType<typeof AppUserDeviceDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const appUserDeviceSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('AppUserDevice Management Detail Component', () => {
    let appUserDeviceServiceStub: SinonStubbedInstance<AppUserDeviceService>;
    let mountOptions: MountingOptions<AppUserDeviceDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      appUserDeviceServiceStub = sinon.createStubInstance<AppUserDeviceService>(AppUserDeviceService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          appUserDeviceService: () => appUserDeviceServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        appUserDeviceServiceStub.find.resolves(appUserDeviceSample);
        route = {
          params: {
            appUserDeviceId: `${123}`,
          },
        };
        const wrapper = shallowMount(AppUserDeviceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.appUserDevice).toMatchObject(appUserDeviceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        appUserDeviceServiceStub.find.resolves(appUserDeviceSample);
        const wrapper = shallowMount(AppUserDeviceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
