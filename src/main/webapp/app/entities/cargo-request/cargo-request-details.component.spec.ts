import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CargoRequestDetails from './cargo-request-details.vue';
import CargoRequestService from './cargo-request.service';
import AlertService from '@/shared/alert/alert.service';

type CargoRequestDetailsComponentType = InstanceType<typeof CargoRequestDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cargoRequestSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CargoRequest Management Detail Component', () => {
    let cargoRequestServiceStub: SinonStubbedInstance<CargoRequestService>;
    let mountOptions: MountingOptions<CargoRequestDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      cargoRequestServiceStub = sinon.createStubInstance<CargoRequestService>(CargoRequestService);

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
          cargoRequestService: () => cargoRequestServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cargoRequestServiceStub.find.resolves(cargoRequestSample);
        route = {
          params: {
            cargoRequestId: `${123}`,
          },
        };
        const wrapper = shallowMount(CargoRequestDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.cargoRequest).toMatchObject(cargoRequestSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cargoRequestServiceStub.find.resolves(cargoRequestSample);
        const wrapper = shallowMount(CargoRequestDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
