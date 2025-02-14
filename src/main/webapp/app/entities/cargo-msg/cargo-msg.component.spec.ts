import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CargoMsg from './cargo-msg.vue';
import CargoMsgService from './cargo-msg.service';
import AlertService from '@/shared/alert/alert.service';

type CargoMsgComponentType = InstanceType<typeof CargoMsg>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CargoMsg Management Component', () => {
    let cargoMsgServiceStub: SinonStubbedInstance<CargoMsgService>;
    let mountOptions: MountingOptions<CargoMsgComponentType>['global'];

    beforeEach(() => {
      cargoMsgServiceStub = sinon.createStubInstance<CargoMsgService>(CargoMsgService);
      cargoMsgServiceStub.retrieve.resolves({ headers: {} });

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
          cargoMsgService: () => cargoMsgServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cargoMsgServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CargoMsg, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(cargoMsgServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.cargoMsgs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(CargoMsg, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(cargoMsgServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: CargoMsgComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CargoMsg, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        cargoMsgServiceStub.retrieve.reset();
        cargoMsgServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        cargoMsgServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(cargoMsgServiceStub.retrieve.called).toBeTruthy();
        expect(comp.cargoMsgs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        cargoMsgServiceStub.retrieve.reset();
        cargoMsgServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(cargoMsgServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.cargoMsgs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(cargoMsgServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        cargoMsgServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCargoMsg();
        await comp.$nextTick(); // clear components

        // THEN
        expect(cargoMsgServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(cargoMsgServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
