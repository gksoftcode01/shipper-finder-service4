import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import SubscribeTypeDetail from './subscribe-type-detail.vue';
import SubscribeTypeDetailService from './subscribe-type-detail.service';
import AlertService from '@/shared/alert/alert.service';

type SubscribeTypeDetailComponentType = InstanceType<typeof SubscribeTypeDetail>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SubscribeTypeDetail Management Component', () => {
    let subscribeTypeDetailServiceStub: SinonStubbedInstance<SubscribeTypeDetailService>;
    let mountOptions: MountingOptions<SubscribeTypeDetailComponentType>['global'];

    beforeEach(() => {
      subscribeTypeDetailServiceStub = sinon.createStubInstance<SubscribeTypeDetailService>(SubscribeTypeDetailService);
      subscribeTypeDetailServiceStub.retrieve.resolves({ headers: {} });

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
          subscribeTypeDetailService: () => subscribeTypeDetailServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        subscribeTypeDetailServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(SubscribeTypeDetail, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(subscribeTypeDetailServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.subscribeTypeDetails[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(SubscribeTypeDetail, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(subscribeTypeDetailServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: SubscribeTypeDetailComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SubscribeTypeDetail, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        subscribeTypeDetailServiceStub.retrieve.reset();
        subscribeTypeDetailServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        subscribeTypeDetailServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(subscribeTypeDetailServiceStub.retrieve.called).toBeTruthy();
        expect(comp.subscribeTypeDetails[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        subscribeTypeDetailServiceStub.retrieve.reset();
        subscribeTypeDetailServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(subscribeTypeDetailServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.subscribeTypeDetails[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(subscribeTypeDetailServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        subscribeTypeDetailServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSubscribeTypeDetail();
        await comp.$nextTick(); // clear components

        // THEN
        expect(subscribeTypeDetailServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(subscribeTypeDetailServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
