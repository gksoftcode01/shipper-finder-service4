import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CargoRequestItemUpdate from './cargo-request-item-update.vue';
import CargoRequestItemService from './cargo-request-item.service';
import AlertService from '@/shared/alert/alert.service';

import ItemService from '@/entities/item/item.service';
import UnitService from '@/entities/unit/unit.service';
import TagService from '@/entities/tag/tag.service';
import CargoRequestService from '@/entities/cargo-request/cargo-request.service';

type CargoRequestItemUpdateComponentType = InstanceType<typeof CargoRequestItemUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cargoRequestItemSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CargoRequestItemUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CargoRequestItem Management Update Component', () => {
    let comp: CargoRequestItemUpdateComponentType;
    let cargoRequestItemServiceStub: SinonStubbedInstance<CargoRequestItemService>;

    beforeEach(() => {
      route = {};
      cargoRequestItemServiceStub = sinon.createStubInstance<CargoRequestItemService>(CargoRequestItemService);
      cargoRequestItemServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          cargoRequestItemService: () => cargoRequestItemServiceStub,
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
          cargoRequestService: () =>
            sinon.createStubInstance<CargoRequestService>(CargoRequestService, {
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
        const wrapper = shallowMount(CargoRequestItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cargoRequestItem = cargoRequestItemSample;
        cargoRequestItemServiceStub.update.resolves(cargoRequestItemSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cargoRequestItemServiceStub.update.calledWith(cargoRequestItemSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        cargoRequestItemServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CargoRequestItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cargoRequestItem = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cargoRequestItemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        cargoRequestItemServiceStub.find.resolves(cargoRequestItemSample);
        cargoRequestItemServiceStub.retrieve.resolves([cargoRequestItemSample]);

        // WHEN
        route = {
          params: {
            cargoRequestItemId: `${cargoRequestItemSample.id}`,
          },
        };
        const wrapper = shallowMount(CargoRequestItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.cargoRequestItem).toMatchObject(cargoRequestItemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cargoRequestItemServiceStub.find.resolves(cargoRequestItemSample);
        const wrapper = shallowMount(CargoRequestItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
