import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import UnitDetails from './unit-details.vue';
import UnitService from './unit.service';
import AlertService from '@/shared/alert/alert.service';

type UnitDetailsComponentType = InstanceType<typeof UnitDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const unitSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Unit Management Detail Component', () => {
    let unitServiceStub: SinonStubbedInstance<UnitService>;
    let mountOptions: MountingOptions<UnitDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      unitServiceStub = sinon.createStubInstance<UnitService>(UnitService);

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
          unitService: () => unitServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        unitServiceStub.find.resolves(unitSample);
        route = {
          params: {
            unitId: `${123}`,
          },
        };
        const wrapper = shallowMount(UnitDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.unit).toMatchObject(unitSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        unitServiceStub.find.resolves(unitSample);
        const wrapper = shallowMount(UnitDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
