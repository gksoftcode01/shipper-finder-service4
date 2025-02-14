import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TripItemUpdate from './trip-item-update.vue';
import TripItemService from './trip-item.service';
import AlertService from '@/shared/alert/alert.service';

import ItemService from '@/entities/item/item.service';
import UnitService from '@/entities/unit/unit.service';
import TagService from '@/entities/tag/tag.service';
import TripService from '@/entities/trip/trip.service';

type TripItemUpdateComponentType = InstanceType<typeof TripItemUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tripItemSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TripItemUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('TripItem Management Update Component', () => {
    let comp: TripItemUpdateComponentType;
    let tripItemServiceStub: SinonStubbedInstance<TripItemService>;

    beforeEach(() => {
      route = {};
      tripItemServiceStub = sinon.createStubInstance<TripItemService>(TripItemService);
      tripItemServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          tripItemService: () => tripItemServiceStub,
          itemService: () =>
            sinon.createStubInstance<ItemService>(ItemService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          unitService: () =>
            sinon.createStubInstance<UnitService>(UnitService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          tagService: () =>
            sinon.createStubInstance<TagService>(TagService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          tripService: () =>
            sinon.createStubInstance<TripService>(TripService, {
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
        const wrapper = shallowMount(TripItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tripItem = tripItemSample;
        tripItemServiceStub.update.resolves(tripItemSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tripItemServiceStub.update.calledWith(tripItemSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        tripItemServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TripItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tripItem = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tripItemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        tripItemServiceStub.find.resolves(tripItemSample);
        tripItemServiceStub.retrieve.resolves([tripItemSample]);

        // WHEN
        route = {
          params: {
            tripItemId: `${tripItemSample.id}`,
          },
        };
        const wrapper = shallowMount(TripItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.tripItem).toMatchObject(tripItemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tripItemServiceStub.find.resolves(tripItemSample);
        const wrapper = shallowMount(TripItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
