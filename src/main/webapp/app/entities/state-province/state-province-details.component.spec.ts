import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import StateProvinceDetails from './state-province-details.vue';
import StateProvinceService from './state-province.service';
import AlertService from '@/shared/alert/alert.service';

type StateProvinceDetailsComponentType = InstanceType<typeof StateProvinceDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const stateProvinceSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('StateProvince Management Detail Component', () => {
    let stateProvinceServiceStub: SinonStubbedInstance<StateProvinceService>;
    let mountOptions: MountingOptions<StateProvinceDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      stateProvinceServiceStub = sinon.createStubInstance<StateProvinceService>(StateProvinceService);

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
          stateProvinceService: () => stateProvinceServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        stateProvinceServiceStub.find.resolves(stateProvinceSample);
        route = {
          params: {
            stateProvinceId: `${123}`,
          },
        };
        const wrapper = shallowMount(StateProvinceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.stateProvince).toMatchObject(stateProvinceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        stateProvinceServiceStub.find.resolves(stateProvinceSample);
        const wrapper = shallowMount(StateProvinceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
