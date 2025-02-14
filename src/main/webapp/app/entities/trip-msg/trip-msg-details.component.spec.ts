import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TripMsgDetails from './trip-msg-details.vue';
import TripMsgService from './trip-msg.service';
import AlertService from '@/shared/alert/alert.service';

type TripMsgDetailsComponentType = InstanceType<typeof TripMsgDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tripMsgSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('TripMsg Management Detail Component', () => {
    let tripMsgServiceStub: SinonStubbedInstance<TripMsgService>;
    let mountOptions: MountingOptions<TripMsgDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      tripMsgServiceStub = sinon.createStubInstance<TripMsgService>(TripMsgService);

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
          tripMsgService: () => tripMsgServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tripMsgServiceStub.find.resolves(tripMsgSample);
        route = {
          params: {
            tripMsgId: `${123}`,
          },
        };
        const wrapper = shallowMount(TripMsgDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.tripMsg).toMatchObject(tripMsgSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tripMsgServiceStub.find.resolves(tripMsgSample);
        const wrapper = shallowMount(TripMsgDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
