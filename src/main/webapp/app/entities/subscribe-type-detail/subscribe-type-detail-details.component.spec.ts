import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscribeTypeDetailDetails from './subscribe-type-detail-details.vue';
import SubscribeTypeDetailService from './subscribe-type-detail.service';
import AlertService from '@/shared/alert/alert.service';

type SubscribeTypeDetailDetailsComponentType = InstanceType<typeof SubscribeTypeDetailDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscribeTypeDetailSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SubscribeTypeDetail Management Detail Component', () => {
    let subscribeTypeDetailServiceStub: SinonStubbedInstance<SubscribeTypeDetailService>;
    let mountOptions: MountingOptions<SubscribeTypeDetailDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      subscribeTypeDetailServiceStub = sinon.createStubInstance<SubscribeTypeDetailService>(SubscribeTypeDetailService);

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
          subscribeTypeDetailService: () => subscribeTypeDetailServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        subscribeTypeDetailServiceStub.find.resolves(subscribeTypeDetailSample);
        route = {
          params: {
            subscribeTypeDetailId: `${123}`,
          },
        };
        const wrapper = shallowMount(SubscribeTypeDetailDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.subscribeTypeDetail).toMatchObject(subscribeTypeDetailSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscribeTypeDetailServiceStub.find.resolves(subscribeTypeDetailSample);
        const wrapper = shallowMount(SubscribeTypeDetailDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
