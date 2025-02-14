import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CargoRequest from './cargo-request.vue';
import CargoRequestService from './cargo-request.service';
import AlertService from '@/shared/alert/alert.service';

type CargoRequestComponentType = InstanceType<typeof CargoRequest>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CargoRequest Management Component', () => {
    let cargoRequestServiceStub: SinonStubbedInstance<CargoRequestService>;
    let mountOptions: MountingOptions<CargoRequestComponentType>['global'];

    beforeEach(() => {
      cargoRequestServiceStub = sinon.createStubInstance<CargoRequestService>(CargoRequestService);
      cargoRequestServiceStub.retrieve.resolves({ headers: {} });

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
          cargoRequestService: () => cargoRequestServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cargoRequestServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CargoRequest, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(cargoRequestServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.cargoRequests[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(CargoRequest, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(cargoRequestServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: CargoRequestComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CargoRequest, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        cargoRequestServiceStub.retrieve.reset();
        cargoRequestServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        cargoRequestServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(cargoRequestServiceStub.retrieve.called).toBeTruthy();
        expect(comp.cargoRequests[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        cargoRequestServiceStub.retrieve.reset();
        cargoRequestServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(cargoRequestServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.cargoRequests[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(cargoRequestServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        cargoRequestServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCargoRequest();
        await comp.$nextTick(); // clear components

        // THEN
        expect(cargoRequestServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(cargoRequestServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
