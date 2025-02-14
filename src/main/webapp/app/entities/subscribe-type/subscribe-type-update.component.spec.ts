import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscribeTypeUpdate from './subscribe-type-update.vue';
import SubscribeTypeService from './subscribe-type.service';
import AlertService from '@/shared/alert/alert.service';

type SubscribeTypeUpdateComponentType = InstanceType<typeof SubscribeTypeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscribeTypeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SubscribeTypeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SubscribeType Management Update Component', () => {
    let comp: SubscribeTypeUpdateComponentType;
    let subscribeTypeServiceStub: SinonStubbedInstance<SubscribeTypeService>;

    beforeEach(() => {
      route = {};
      subscribeTypeServiceStub = sinon.createStubInstance<SubscribeTypeService>(SubscribeTypeService);
      subscribeTypeServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          subscribeTypeService: () => subscribeTypeServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(SubscribeTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscribeType = subscribeTypeSample;
        subscribeTypeServiceStub.update.resolves(subscribeTypeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscribeTypeServiceStub.update.calledWith(subscribeTypeSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        subscribeTypeServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SubscribeTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscribeType = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscribeTypeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        subscribeTypeServiceStub.find.resolves(subscribeTypeSample);
        subscribeTypeServiceStub.retrieve.resolves([subscribeTypeSample]);

        // WHEN
        route = {
          params: {
            subscribeTypeId: `${subscribeTypeSample.id}`,
          },
        };
        const wrapper = shallowMount(SubscribeTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.subscribeType).toMatchObject(subscribeTypeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscribeTypeServiceStub.find.resolves(subscribeTypeSample);
        const wrapper = shallowMount(SubscribeTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
