import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscribeTypeDetailUpdate from './subscribe-type-detail-update.vue';
import SubscribeTypeDetailService from './subscribe-type-detail.service';
import AlertService from '@/shared/alert/alert.service';

import SubscribeTypeService from '@/entities/subscribe-type/subscribe-type.service';

type SubscribeTypeDetailUpdateComponentType = InstanceType<typeof SubscribeTypeDetailUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscribeTypeDetailSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SubscribeTypeDetailUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SubscribeTypeDetail Management Update Component', () => {
    let comp: SubscribeTypeDetailUpdateComponentType;
    let subscribeTypeDetailServiceStub: SinonStubbedInstance<SubscribeTypeDetailService>;

    beforeEach(() => {
      route = {};
      subscribeTypeDetailServiceStub = sinon.createStubInstance<SubscribeTypeDetailService>(SubscribeTypeDetailService);
      subscribeTypeDetailServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          subscribeTypeDetailService: () => subscribeTypeDetailServiceStub,
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

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(SubscribeTypeDetailUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscribeTypeDetail = subscribeTypeDetailSample;
        subscribeTypeDetailServiceStub.update.resolves(subscribeTypeDetailSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscribeTypeDetailServiceStub.update.calledWith(subscribeTypeDetailSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        subscribeTypeDetailServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SubscribeTypeDetailUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscribeTypeDetail = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscribeTypeDetailServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        subscribeTypeDetailServiceStub.find.resolves(subscribeTypeDetailSample);
        subscribeTypeDetailServiceStub.retrieve.resolves([subscribeTypeDetailSample]);

        // WHEN
        route = {
          params: {
            subscribeTypeDetailId: `${subscribeTypeDetailSample.id}`,
          },
        };
        const wrapper = shallowMount(SubscribeTypeDetailUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.subscribeTypeDetail).toMatchObject(subscribeTypeDetailSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscribeTypeDetailServiceStub.find.resolves(subscribeTypeDetailSample);
        const wrapper = shallowMount(SubscribeTypeDetailUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
