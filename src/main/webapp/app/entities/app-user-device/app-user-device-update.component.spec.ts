import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import AppUserDeviceUpdate from './app-user-device-update.vue';
import AppUserDeviceService from './app-user-device.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

type AppUserDeviceUpdateComponentType = InstanceType<typeof AppUserDeviceUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const appUserDeviceSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AppUserDeviceUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('AppUserDevice Management Update Component', () => {
    let comp: AppUserDeviceUpdateComponentType;
    let appUserDeviceServiceStub: SinonStubbedInstance<AppUserDeviceService>;

    beforeEach(() => {
      route = {};
      appUserDeviceServiceStub = sinon.createStubInstance<AppUserDeviceService>(AppUserDeviceService);
      appUserDeviceServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          appUserDeviceService: () => appUserDeviceServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(AppUserDeviceUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(AppUserDeviceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.appUserDevice = appUserDeviceSample;
        appUserDeviceServiceStub.update.resolves(appUserDeviceSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(appUserDeviceServiceStub.update.calledWith(appUserDeviceSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        appUserDeviceServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AppUserDeviceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.appUserDevice = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(appUserDeviceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        appUserDeviceServiceStub.find.resolves(appUserDeviceSample);
        appUserDeviceServiceStub.retrieve.resolves([appUserDeviceSample]);

        // WHEN
        route = {
          params: {
            appUserDeviceId: `${appUserDeviceSample.id}`,
          },
        };
        const wrapper = shallowMount(AppUserDeviceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.appUserDevice).toMatchObject(appUserDeviceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        appUserDeviceServiceStub.find.resolves(appUserDeviceSample);
        const wrapper = shallowMount(AppUserDeviceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
