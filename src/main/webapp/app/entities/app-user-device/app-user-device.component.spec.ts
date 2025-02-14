import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AppUserDevice from './app-user-device.vue';
import AppUserDeviceService from './app-user-device.service';
import AlertService from '@/shared/alert/alert.service';

type AppUserDeviceComponentType = InstanceType<typeof AppUserDevice>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('AppUserDevice Management Component', () => {
    let appUserDeviceServiceStub: SinonStubbedInstance<AppUserDeviceService>;
    let mountOptions: MountingOptions<AppUserDeviceComponentType>['global'];

    beforeEach(() => {
      appUserDeviceServiceStub = sinon.createStubInstance<AppUserDeviceService>(AppUserDeviceService);
      appUserDeviceServiceStub.retrieve.resolves({ headers: {} });

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
          appUserDeviceService: () => appUserDeviceServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        appUserDeviceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(AppUserDevice, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(appUserDeviceServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.appUserDevices[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(AppUserDevice, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(appUserDeviceServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: AppUserDeviceComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(AppUserDevice, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        appUserDeviceServiceStub.retrieve.reset();
        appUserDeviceServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        appUserDeviceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(appUserDeviceServiceStub.retrieve.called).toBeTruthy();
        expect(comp.appUserDevices[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        appUserDeviceServiceStub.retrieve.reset();
        appUserDeviceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(appUserDeviceServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.appUserDevices[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(appUserDeviceServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        appUserDeviceServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAppUserDevice();
        await comp.$nextTick(); // clear components

        // THEN
        expect(appUserDeviceServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(appUserDeviceServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
