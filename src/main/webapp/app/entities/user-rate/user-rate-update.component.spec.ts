import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import UserRateUpdate from './user-rate-update.vue';
import UserRateService from './user-rate.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

type UserRateUpdateComponentType = InstanceType<typeof UserRateUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const userRateSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<UserRateUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('UserRate Management Update Component', () => {
    let comp: UserRateUpdateComponentType;
    let userRateServiceStub: SinonStubbedInstance<UserRateService>;

    beforeEach(() => {
      route = {};
      userRateServiceStub = sinon.createStubInstance<UserRateService>(UserRateService);
      userRateServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          userRateService: () => userRateServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(UserRateUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(UserRateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.userRate = userRateSample;
        userRateServiceStub.update.resolves(userRateSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(userRateServiceStub.update.calledWith(userRateSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        userRateServiceStub.create.resolves(entity);
        const wrapper = shallowMount(UserRateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.userRate = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(userRateServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        userRateServiceStub.find.resolves(userRateSample);
        userRateServiceStub.retrieve.resolves([userRateSample]);

        // WHEN
        route = {
          params: {
            userRateId: `${userRateSample.id}`,
          },
        };
        const wrapper = shallowMount(UserRateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.userRate).toMatchObject(userRateSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        userRateServiceStub.find.resolves(userRateSample);
        const wrapper = shallowMount(UserRateUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
