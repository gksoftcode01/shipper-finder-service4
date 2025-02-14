import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CargoMsgDetails from './cargo-msg-details.vue';
import CargoMsgService from './cargo-msg.service';
import AlertService from '@/shared/alert/alert.service';

type CargoMsgDetailsComponentType = InstanceType<typeof CargoMsgDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cargoMsgSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CargoMsg Management Detail Component', () => {
    let cargoMsgServiceStub: SinonStubbedInstance<CargoMsgService>;
    let mountOptions: MountingOptions<CargoMsgDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      cargoMsgServiceStub = sinon.createStubInstance<CargoMsgService>(CargoMsgService);

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
          cargoMsgService: () => cargoMsgServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cargoMsgServiceStub.find.resolves(cargoMsgSample);
        route = {
          params: {
            cargoMsgId: `${123}`,
          },
        };
        const wrapper = shallowMount(CargoMsgDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.cargoMsg).toMatchObject(cargoMsgSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cargoMsgServiceStub.find.resolves(cargoMsgSample);
        const wrapper = shallowMount(CargoMsgDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
