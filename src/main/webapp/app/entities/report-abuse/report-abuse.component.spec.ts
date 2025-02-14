import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ReportAbuse from './report-abuse.vue';
import ReportAbuseService from './report-abuse.service';
import AlertService from '@/shared/alert/alert.service';

type ReportAbuseComponentType = InstanceType<typeof ReportAbuse>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ReportAbuse Management Component', () => {
    let reportAbuseServiceStub: SinonStubbedInstance<ReportAbuseService>;
    let mountOptions: MountingOptions<ReportAbuseComponentType>['global'];

    beforeEach(() => {
      reportAbuseServiceStub = sinon.createStubInstance<ReportAbuseService>(ReportAbuseService);
      reportAbuseServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          reportAbuseService: () => reportAbuseServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        reportAbuseServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ReportAbuse, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(reportAbuseServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.reportAbuses[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(ReportAbuse, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(reportAbuseServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: ReportAbuseComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ReportAbuse, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        reportAbuseServiceStub.retrieve.reset();
        reportAbuseServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        reportAbuseServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(reportAbuseServiceStub.retrieve.called).toBeTruthy();
        expect(comp.reportAbuses[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        reportAbuseServiceStub.retrieve.reset();
        reportAbuseServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(reportAbuseServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.reportAbuses[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(reportAbuseServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        reportAbuseServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeReportAbuse();
        await comp.$nextTick(); // clear components

        // THEN
        expect(reportAbuseServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(reportAbuseServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
