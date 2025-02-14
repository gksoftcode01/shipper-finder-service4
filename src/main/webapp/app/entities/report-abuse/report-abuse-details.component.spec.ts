import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ReportAbuseDetails from './report-abuse-details.vue';
import ReportAbuseService from './report-abuse.service';
import AlertService from '@/shared/alert/alert.service';

type ReportAbuseDetailsComponentType = InstanceType<typeof ReportAbuseDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const reportAbuseSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ReportAbuse Management Detail Component', () => {
    let reportAbuseServiceStub: SinonStubbedInstance<ReportAbuseService>;
    let mountOptions: MountingOptions<ReportAbuseDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      reportAbuseServiceStub = sinon.createStubInstance<ReportAbuseService>(ReportAbuseService);

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
          reportAbuseService: () => reportAbuseServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        reportAbuseServiceStub.find.resolves(reportAbuseSample);
        route = {
          params: {
            reportAbuseId: `${123}`,
          },
        };
        const wrapper = shallowMount(ReportAbuseDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.reportAbuse).toMatchObject(reportAbuseSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        reportAbuseServiceStub.find.resolves(reportAbuseSample);
        const wrapper = shallowMount(ReportAbuseDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
