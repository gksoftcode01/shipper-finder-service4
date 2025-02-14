import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import UserSubscribeDetails from './user-subscribe-details.vue';
import UserSubscribeService from './user-subscribe.service';
import AlertService from '@/shared/alert/alert.service';

type UserSubscribeDetailsComponentType = InstanceType<typeof UserSubscribeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const userSubscribeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('UserSubscribe Management Detail Component', () => {
    let userSubscribeServiceStub: SinonStubbedInstance<UserSubscribeService>;
    let mountOptions: MountingOptions<UserSubscribeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      userSubscribeServiceStub = sinon.createStubInstance<UserSubscribeService>(UserSubscribeService);

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
          userSubscribeService: () => userSubscribeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        userSubscribeServiceStub.find.resolves(userSubscribeSample);
        route = {
          params: {
            userSubscribeId: `${123}`,
          },
        };
        const wrapper = shallowMount(UserSubscribeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.userSubscribe).toMatchObject(userSubscribeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        userSubscribeServiceStub.find.resolves(userSubscribeSample);
        const wrapper = shallowMount(UserSubscribeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
