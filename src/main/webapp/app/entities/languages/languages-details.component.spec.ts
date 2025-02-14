import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import LanguagesDetails from './languages-details.vue';
import LanguagesService from './languages.service';
import AlertService from '@/shared/alert/alert.service';

type LanguagesDetailsComponentType = InstanceType<typeof LanguagesDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const languagesSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Languages Management Detail Component', () => {
    let languagesServiceStub: SinonStubbedInstance<LanguagesService>;
    let mountOptions: MountingOptions<LanguagesDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      languagesServiceStub = sinon.createStubInstance<LanguagesService>(LanguagesService);

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
          languagesService: () => languagesServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        languagesServiceStub.find.resolves(languagesSample);
        route = {
          params: {
            languagesId: `${123}`,
          },
        };
        const wrapper = shallowMount(LanguagesDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.languages).toMatchObject(languagesSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        languagesServiceStub.find.resolves(languagesSample);
        const wrapper = shallowMount(LanguagesDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
