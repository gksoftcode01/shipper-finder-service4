import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import UserRateDetails from './user-rate-details.vue';
import UserRateService from './user-rate.service';
import AlertService from '@/shared/alert/alert.service';

type UserRateDetailsComponentType = InstanceType<typeof UserRateDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const userRateSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('UserRate Management Detail Component', () => {
    let userRateServiceStub: SinonStubbedInstance<UserRateService>;
    let mountOptions: MountingOptions<UserRateDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      userRateServiceStub = sinon.createStubInstance<UserRateService>(UserRateService);

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
          userRateService: () => userRateServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        userRateServiceStub.find.resolves(userRateSample);
        route = {
          params: {
            userRateId: `${123}`,
          },
        };
        const wrapper = shallowMount(UserRateDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.userRate).toMatchObject(userRateSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        userRateServiceStub.find.resolves(userRateSample);
        const wrapper = shallowMount(UserRateDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
