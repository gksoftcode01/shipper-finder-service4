import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ShowNumberHistory from './show-number-history.vue';
import ShowNumberHistoryService from './show-number-history.service';
import AlertService from '@/shared/alert/alert.service';

type ShowNumberHistoryComponentType = InstanceType<typeof ShowNumberHistory>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ShowNumberHistory Management Component', () => {
    let showNumberHistoryServiceStub: SinonStubbedInstance<ShowNumberHistoryService>;
    let mountOptions: MountingOptions<ShowNumberHistoryComponentType>['global'];

    beforeEach(() => {
      showNumberHistoryServiceStub = sinon.createStubInstance<ShowNumberHistoryService>(ShowNumberHistoryService);
      showNumberHistoryServiceStub.retrieve.resolves({ headers: {} });

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
          showNumberHistoryService: () => showNumberHistoryServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        showNumberHistoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ShowNumberHistory, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(showNumberHistoryServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.showNumberHistories[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(ShowNumberHistory, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(showNumberHistoryServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: ShowNumberHistoryComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ShowNumberHistory, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        showNumberHistoryServiceStub.retrieve.reset();
        showNumberHistoryServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        showNumberHistoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(showNumberHistoryServiceStub.retrieve.called).toBeTruthy();
        expect(comp.showNumberHistories[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        showNumberHistoryServiceStub.retrieve.reset();
        showNumberHistoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(showNumberHistoryServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.showNumberHistories[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(showNumberHistoryServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        showNumberHistoryServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeShowNumberHistory();
        await comp.$nextTick(); // clear components

        // THEN
        expect(showNumberHistoryServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(showNumberHistoryServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
