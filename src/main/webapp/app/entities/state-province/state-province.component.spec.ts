import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import StateProvince from './state-province.vue';
import StateProvinceService from './state-province.service';
import AlertService from '@/shared/alert/alert.service';

type StateProvinceComponentType = InstanceType<typeof StateProvince>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('StateProvince Management Component', () => {
    let stateProvinceServiceStub: SinonStubbedInstance<StateProvinceService>;
    let mountOptions: MountingOptions<StateProvinceComponentType>['global'];

    beforeEach(() => {
      stateProvinceServiceStub = sinon.createStubInstance<StateProvinceService>(StateProvinceService);
      stateProvinceServiceStub.retrieve.resolves({ headers: {} });

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
          stateProvinceService: () => stateProvinceServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        stateProvinceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(StateProvince, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(stateProvinceServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.stateProvinces[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(StateProvince, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(stateProvinceServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: StateProvinceComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(StateProvince, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        stateProvinceServiceStub.retrieve.reset();
        stateProvinceServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        stateProvinceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(stateProvinceServiceStub.retrieve.called).toBeTruthy();
        expect(comp.stateProvinces[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        stateProvinceServiceStub.retrieve.reset();
        stateProvinceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(stateProvinceServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.stateProvinces[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(stateProvinceServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        stateProvinceServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeStateProvince();
        await comp.$nextTick(); // clear components

        // THEN
        expect(stateProvinceServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(stateProvinceServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
