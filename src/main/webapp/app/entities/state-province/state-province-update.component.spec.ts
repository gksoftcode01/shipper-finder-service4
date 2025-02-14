import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import StateProvinceUpdate from './state-province-update.vue';
import StateProvinceService from './state-province.service';
import AlertService from '@/shared/alert/alert.service';

import CountryService from '@/entities/country/country.service';

type StateProvinceUpdateComponentType = InstanceType<typeof StateProvinceUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const stateProvinceSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<StateProvinceUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('StateProvince Management Update Component', () => {
    let comp: StateProvinceUpdateComponentType;
    let stateProvinceServiceStub: SinonStubbedInstance<StateProvinceService>;

    beforeEach(() => {
      route = {};
      stateProvinceServiceStub = sinon.createStubInstance<StateProvinceService>(StateProvinceService);
      stateProvinceServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          stateProvinceService: () => stateProvinceServiceStub,
          countryService: () =>
            sinon.createStubInstance<CountryService>(CountryService, {
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
        const wrapper = shallowMount(StateProvinceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.stateProvince = stateProvinceSample;
        stateProvinceServiceStub.update.resolves(stateProvinceSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(stateProvinceServiceStub.update.calledWith(stateProvinceSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        stateProvinceServiceStub.create.resolves(entity);
        const wrapper = shallowMount(StateProvinceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.stateProvince = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(stateProvinceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        stateProvinceServiceStub.find.resolves(stateProvinceSample);
        stateProvinceServiceStub.retrieve.resolves([stateProvinceSample]);

        // WHEN
        route = {
          params: {
            stateProvinceId: `${stateProvinceSample.id}`,
          },
        };
        const wrapper = shallowMount(StateProvinceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.stateProvince).toMatchObject(stateProvinceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        stateProvinceServiceStub.find.resolves(stateProvinceSample);
        const wrapper = shallowMount(StateProvinceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
