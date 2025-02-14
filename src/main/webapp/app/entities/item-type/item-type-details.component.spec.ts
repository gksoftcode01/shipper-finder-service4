import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ItemTypeDetails from './item-type-details.vue';
import ItemTypeService from './item-type.service';
import AlertService from '@/shared/alert/alert.service';

type ItemTypeDetailsComponentType = InstanceType<typeof ItemTypeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const itemTypeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ItemType Management Detail Component', () => {
    let itemTypeServiceStub: SinonStubbedInstance<ItemTypeService>;
    let mountOptions: MountingOptions<ItemTypeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      itemTypeServiceStub = sinon.createStubInstance<ItemTypeService>(ItemTypeService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          itemTypeService: () => itemTypeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        itemTypeServiceStub.find.resolves(itemTypeSample);
        route = {
          params: {
            itemTypeId: `${123}`,
          },
        };
        const wrapper = shallowMount(ItemTypeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.itemType).toMatchObject(itemTypeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        itemTypeServiceStub.find.resolves(itemTypeSample);
        const wrapper = shallowMount(ItemTypeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
