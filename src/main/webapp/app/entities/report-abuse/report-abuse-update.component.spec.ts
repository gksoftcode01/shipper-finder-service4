import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import ReportAbuseUpdate from './report-abuse-update.vue';
import ReportAbuseService from './report-abuse.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

type ReportAbuseUpdateComponentType = InstanceType<typeof ReportAbuseUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const reportAbuseSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ReportAbuseUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ReportAbuse Management Update Component', () => {
    let comp: ReportAbuseUpdateComponentType;
    let reportAbuseServiceStub: SinonStubbedInstance<ReportAbuseService>;

    beforeEach(() => {
      route = {};
      reportAbuseServiceStub = sinon.createStubInstance<ReportAbuseService>(ReportAbuseService);
      reportAbuseServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          reportAbuseService: () => reportAbuseServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(ReportAbuseUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ReportAbuseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.reportAbuse = reportAbuseSample;
        reportAbuseServiceStub.update.resolves(reportAbuseSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(reportAbuseServiceStub.update.calledWith(reportAbuseSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        reportAbuseServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ReportAbuseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.reportAbuse = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(reportAbuseServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        reportAbuseServiceStub.find.resolves(reportAbuseSample);
        reportAbuseServiceStub.retrieve.resolves([reportAbuseSample]);

        // WHEN
        route = {
          params: {
            reportAbuseId: `${reportAbuseSample.id}`,
          },
        };
        const wrapper = shallowMount(ReportAbuseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.reportAbuse).toMatchObject(reportAbuseSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        reportAbuseServiceStub.find.resolves(reportAbuseSample);
        const wrapper = shallowMount(ReportAbuseUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
