import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import AppUserUpdate from './app-user-update.vue';
import AppUserService from './app-user.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import LanguagesService from '@/entities/languages/languages.service';
import CountryService from '@/entities/country/country.service';

type AppUserUpdateComponentType = InstanceType<typeof AppUserUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const appUserSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AppUserUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('AppUser Management Update Component', () => {
    let comp: AppUserUpdateComponentType;
    let appUserServiceStub: SinonStubbedInstance<AppUserService>;

    beforeEach(() => {
      route = {};
      appUserServiceStub = sinon.createStubInstance<AppUserService>(AppUserService);
      appUserServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          appUserService: () => appUserServiceStub,
          languagesService: () =>
            sinon.createStubInstance<LanguagesService>(LanguagesService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          countryService: () =>
            sinon.createStubInstance<CountryService>(CountryService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(AppUserUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(AppUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.appUser = appUserSample;
        appUserServiceStub.update.resolves(appUserSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(appUserServiceStub.update.calledWith(appUserSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        appUserServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AppUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.appUser = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(appUserServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        appUserServiceStub.find.resolves(appUserSample);
        appUserServiceStub.retrieve.resolves([appUserSample]);

        // WHEN
        route = {
          params: {
            appUserId: `${appUserSample.id}`,
          },
        };
        const wrapper = shallowMount(AppUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.appUser).toMatchObject(appUserSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        appUserServiceStub.find.resolves(appUserSample);
        const wrapper = shallowMount(AppUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
