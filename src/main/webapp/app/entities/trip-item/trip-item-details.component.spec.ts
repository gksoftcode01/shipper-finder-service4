import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TripItemDetails from './trip-item-details.vue';
import TripItemService from './trip-item.service';
import AlertService from '@/shared/alert/alert.service';

type TripItemDetailsComponentType = InstanceType<typeof TripItemDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tripItemSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('TripItem Management Detail Component', () => {
    let tripItemServiceStub: SinonStubbedInstance<TripItemService>;
    let mountOptions: MountingOptions<TripItemDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      tripItemServiceStub = sinon.createStubInstance<TripItemService>(TripItemService);

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
          tripItemService: () => tripItemServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tripItemServiceStub.find.resolves(tripItemSample);
        route = {
          params: {
            tripItemId: `${123}`,
          },
        };
        const wrapper = shallowMount(TripItemDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.tripItem).toMatchObject(tripItemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tripItemServiceStub.find.resolves(tripItemSample);
        const wrapper = shallowMount(TripItemDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
