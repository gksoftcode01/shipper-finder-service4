import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CargoRequestItem from './cargo-request-item.vue';
import CargoRequestItemService from './cargo-request-item.service';
import AlertService from '@/shared/alert/alert.service';

type CargoRequestItemComponentType = InstanceType<typeof CargoRequestItem>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CargoRequestItem Management Component', () => {
    let cargoRequestItemServiceStub: SinonStubbedInstance<CargoRequestItemService>;
    let mountOptions: MountingOptions<CargoRequestItemComponentType>['global'];

    beforeEach(() => {
      cargoRequestItemServiceStub = sinon.createStubInstance<CargoRequestItemService>(CargoRequestItemService);
      cargoRequestItemServiceStub.retrieve.resolves({ headers: {} });

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
          cargoRequestItemService: () => cargoRequestItemServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cargoRequestItemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CargoRequestItem, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(cargoRequestItemServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.cargoRequestItems[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(CargoRequestItem, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(cargoRequestItemServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: CargoRequestItemComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CargoRequestItem, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        cargoRequestItemServiceStub.retrieve.reset();
        cargoRequestItemServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        cargoRequestItemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(cargoRequestItemServiceStub.retrieve.called).toBeTruthy();
        expect(comp.cargoRequestItems[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        cargoRequestItemServiceStub.retrieve.reset();
        cargoRequestItemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(cargoRequestItemServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.cargoRequestItems[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(cargoRequestItemServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        cargoRequestItemServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCargoRequestItem();
        await comp.$nextTick(); // clear components

        // THEN
        expect(cargoRequestItemServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(cargoRequestItemServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
