import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TripDetails from './trip-details.vue';
import TripService from './trip.service';
import AlertService from '@/shared/alert/alert.service';

type TripDetailsComponentType = InstanceType<typeof TripDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tripSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Trip Management Detail Component', () => {
    let tripServiceStub: SinonStubbedInstance<TripService>;
    let mountOptions: MountingOptions<TripDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      tripServiceStub = sinon.createStubInstance<TripService>(TripService);

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
          tripService: () => tripServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tripServiceStub.find.resolves(tripSample);
        route = {
          params: {
            tripId: `${123}`,
          },
        };
        const wrapper = shallowMount(TripDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.trip).toMatchObject(tripSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tripServiceStub.find.resolves(tripSample);
        const wrapper = shallowMount(TripDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
