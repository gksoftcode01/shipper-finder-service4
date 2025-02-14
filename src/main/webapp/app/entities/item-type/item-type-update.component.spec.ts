import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ItemTypeUpdate from './item-type-update.vue';
import ItemTypeService from './item-type.service';
import AlertService from '@/shared/alert/alert.service';

type ItemTypeUpdateComponentType = InstanceType<typeof ItemTypeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const itemTypeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ItemTypeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ItemType Management Update Component', () => {
    let comp: ItemTypeUpdateComponentType;
    let itemTypeServiceStub: SinonStubbedInstance<ItemTypeService>;

    beforeEach(() => {
      route = {};
      itemTypeServiceStub = sinon.createStubInstance<ItemTypeService>(ItemTypeService);
      itemTypeServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          itemTypeService: () => itemTypeServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ItemTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.itemType = itemTypeSample;
        itemTypeServiceStub.update.resolves(itemTypeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(itemTypeServiceStub.update.calledWith(itemTypeSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        itemTypeServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ItemTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.itemType = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(itemTypeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        itemTypeServiceStub.find.resolves(itemTypeSample);
        itemTypeServiceStub.retrieve.resolves([itemTypeSample]);

        // WHEN
        route = {
          params: {
            itemTypeId: `${itemTypeSample.id}`,
          },
        };
        const wrapper = shallowMount(ItemTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.itemType).toMatchObject(itemTypeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        itemTypeServiceStub.find.resolves(itemTypeSample);
        const wrapper = shallowMount(ItemTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
