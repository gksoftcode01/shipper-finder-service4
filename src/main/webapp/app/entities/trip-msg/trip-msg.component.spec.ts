import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import TripMsg from './trip-msg.vue';
import TripMsgService from './trip-msg.service';
import AlertService from '@/shared/alert/alert.service';

type TripMsgComponentType = InstanceType<typeof TripMsg>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('TripMsg Management Component', () => {
    let tripMsgServiceStub: SinonStubbedInstance<TripMsgService>;
    let mountOptions: MountingOptions<TripMsgComponentType>['global'];

    beforeEach(() => {
      tripMsgServiceStub = sinon.createStubInstance<TripMsgService>(TripMsgService);
      tripMsgServiceStub.retrieve.resolves({ headers: {} });

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
          tripMsgService: () => tripMsgServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tripMsgServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(TripMsg, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(tripMsgServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.tripMsgs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(TripMsg, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(tripMsgServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: TripMsgComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(TripMsg, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        tripMsgServiceStub.retrieve.reset();
        tripMsgServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        tripMsgServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(tripMsgServiceStub.retrieve.called).toBeTruthy();
        expect(comp.tripMsgs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        tripMsgServiceStub.retrieve.reset();
        tripMsgServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(tripMsgServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.tripMsgs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(tripMsgServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        tripMsgServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeTripMsg();
        await comp.$nextTick(); // clear components

        // THEN
        expect(tripMsgServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(tripMsgServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
