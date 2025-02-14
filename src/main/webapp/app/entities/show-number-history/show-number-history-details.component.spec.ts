import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ShowNumberHistoryDetails from './show-number-history-details.vue';
import ShowNumberHistoryService from './show-number-history.service';
import AlertService from '@/shared/alert/alert.service';

type ShowNumberHistoryDetailsComponentType = InstanceType<typeof ShowNumberHistoryDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const showNumberHistorySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ShowNumberHistory Management Detail Component', () => {
    let showNumberHistoryServiceStub: SinonStubbedInstance<ShowNumberHistoryService>;
    let mountOptions: MountingOptions<ShowNumberHistoryDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      showNumberHistoryServiceStub = sinon.createStubInstance<ShowNumberHistoryService>(ShowNumberHistoryService);

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
          showNumberHistoryService: () => showNumberHistoryServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        showNumberHistoryServiceStub.find.resolves(showNumberHistorySample);
        route = {
          params: {
            showNumberHistoryId: `${123}`,
          },
        };
        const wrapper = shallowMount(ShowNumberHistoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.showNumberHistory).toMatchObject(showNumberHistorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        showNumberHistoryServiceStub.find.resolves(showNumberHistorySample);
        const wrapper = shallowMount(ShowNumberHistoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
