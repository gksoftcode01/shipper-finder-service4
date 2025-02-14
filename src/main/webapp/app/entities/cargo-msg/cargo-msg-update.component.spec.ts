import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CargoMsgUpdate from './cargo-msg-update.vue';
import CargoMsgService from './cargo-msg.service';
import AlertService from '@/shared/alert/alert.service';

type CargoMsgUpdateComponentType = InstanceType<typeof CargoMsgUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cargoMsgSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CargoMsgUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CargoMsg Management Update Component', () => {
    let comp: CargoMsgUpdateComponentType;
    let cargoMsgServiceStub: SinonStubbedInstance<CargoMsgService>;

    beforeEach(() => {
      route = {};
      cargoMsgServiceStub = sinon.createStubInstance<CargoMsgService>(CargoMsgService);
      cargoMsgServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          cargoMsgService: () => cargoMsgServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CargoMsgUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cargoMsg = cargoMsgSample;
        cargoMsgServiceStub.update.resolves(cargoMsgSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cargoMsgServiceStub.update.calledWith(cargoMsgSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        cargoMsgServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CargoMsgUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cargoMsg = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cargoMsgServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        cargoMsgServiceStub.find.resolves(cargoMsgSample);
        cargoMsgServiceStub.retrieve.resolves([cargoMsgSample]);

        // WHEN
        route = {
          params: {
            cargoMsgId: `${cargoMsgSample.id}`,
          },
        };
        const wrapper = shallowMount(CargoMsgUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.cargoMsg).toMatchObject(cargoMsgSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cargoMsgServiceStub.find.resolves(cargoMsgSample);
        const wrapper = shallowMount(CargoMsgUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
