import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import UnitUpdate from './unit-update.vue';
import UnitService from './unit.service';
import AlertService from '@/shared/alert/alert.service';

type UnitUpdateComponentType = InstanceType<typeof UnitUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const unitSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<UnitUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Unit Management Update Component', () => {
    let comp: UnitUpdateComponentType;
    let unitServiceStub: SinonStubbedInstance<UnitService>;

    beforeEach(() => {
      route = {};
      unitServiceStub = sinon.createStubInstance<UnitService>(UnitService);
      unitServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          unitService: () => unitServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(UnitUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.unit = unitSample;
        unitServiceStub.update.resolves(unitSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(unitServiceStub.update.calledWith(unitSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        unitServiceStub.create.resolves(entity);
        const wrapper = shallowMount(UnitUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.unit = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(unitServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        unitServiceStub.find.resolves(unitSample);
        unitServiceStub.retrieve.resolves([unitSample]);

        // WHEN
        route = {
          params: {
            unitId: `${unitSample.id}`,
          },
        };
        const wrapper = shallowMount(UnitUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.unit).toMatchObject(unitSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        unitServiceStub.find.resolves(unitSample);
        const wrapper = shallowMount(UnitUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
