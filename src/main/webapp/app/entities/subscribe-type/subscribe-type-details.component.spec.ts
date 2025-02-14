import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscribeTypeDetails from './subscribe-type-details.vue';
import SubscribeTypeService from './subscribe-type.service';
import AlertService from '@/shared/alert/alert.service';

type SubscribeTypeDetailsComponentType = InstanceType<typeof SubscribeTypeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscribeTypeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SubscribeType Management Detail Component', () => {
    let subscribeTypeServiceStub: SinonStubbedInstance<SubscribeTypeService>;
    let mountOptions: MountingOptions<SubscribeTypeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      subscribeTypeServiceStub = sinon.createStubInstance<SubscribeTypeService>(SubscribeTypeService);

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
          subscribeTypeService: () => subscribeTypeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        subscribeTypeServiceStub.find.resolves(subscribeTypeSample);
        route = {
          params: {
            subscribeTypeId: `${123}`,
          },
        };
        const wrapper = shallowMount(SubscribeTypeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.subscribeType).toMatchObject(subscribeTypeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscribeTypeServiceStub.find.resolves(subscribeTypeSample);
        const wrapper = shallowMount(SubscribeTypeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
