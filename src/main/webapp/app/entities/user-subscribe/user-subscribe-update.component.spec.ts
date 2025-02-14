import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import UserSubscribeUpdate from './user-subscribe-update.vue';
import UserSubscribeService from './user-subscribe.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import SubscribeTypeService from '@/entities/subscribe-type/subscribe-type.service';

type UserSubscribeUpdateComponentType = InstanceType<typeof UserSubscribeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const userSubscribeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<UserSubscribeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('UserSubscribe Management Update Component', () => {
    let comp: UserSubscribeUpdateComponentType;
    let userSubscribeServiceStub: SinonStubbedInstance<UserSubscribeService>;

    beforeEach(() => {
      route = {};
      userSubscribeServiceStub = sinon.createStubInstance<UserSubscribeService>(UserSubscribeService);
      userSubscribeServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          userSubscribeService: () => userSubscribeServiceStub,
          subscribeTypeService: () =>
            sinon.createStubInstance<SubscribeTypeService>(SubscribeTypeService, {
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
        const wrapper = shallowMount(UserSubscribeUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(UserSubscribeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.userSubscribe = userSubscribeSample;
        userSubscribeServiceStub.update.resolves(userSubscribeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(userSubscribeServiceStub.update.calledWith(userSubscribeSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        userSubscribeServiceStub.create.resolves(entity);
        const wrapper = shallowMount(UserSubscribeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.userSubscribe = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(userSubscribeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        userSubscribeServiceStub.find.resolves(userSubscribeSample);
        userSubscribeServiceStub.retrieve.resolves([userSubscribeSample]);

        // WHEN
        route = {
          params: {
            userSubscribeId: `${userSubscribeSample.id}`,
          },
        };
        const wrapper = shallowMount(UserSubscribeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.userSubscribe).toMatchObject(userSubscribeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        userSubscribeServiceStub.find.resolves(userSubscribeSample);
        const wrapper = shallowMount(UserSubscribeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
