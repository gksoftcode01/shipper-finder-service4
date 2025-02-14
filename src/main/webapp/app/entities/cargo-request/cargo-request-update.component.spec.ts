import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import CargoRequestUpdate from './cargo-request-update.vue';
import CargoRequestService from './cargo-request.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import CountryService from '@/entities/country/country.service';
import StateProvinceService from '@/entities/state-province/state-province.service';

type CargoRequestUpdateComponentType = InstanceType<typeof CargoRequestUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cargoRequestSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CargoRequestUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CargoRequest Management Update Component', () => {
    let comp: CargoRequestUpdateComponentType;
    let cargoRequestServiceStub: SinonStubbedInstance<CargoRequestService>;

    beforeEach(() => {
      route = {};
      cargoRequestServiceStub = sinon.createStubInstance<CargoRequestService>(CargoRequestService);
      cargoRequestServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          cargoRequestService: () => cargoRequestServiceStub,
          countryService: () =>
            sinon.createStubInstance<CountryService>(CountryService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          stateProvinceService: () =>
            sinon.createStubInstance<StateProvinceService>(StateProvinceService, {
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
        const wrapper = shallowMount(CargoRequestUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(CargoRequestUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cargoRequest = cargoRequestSample;
        cargoRequestServiceStub.update.resolves(cargoRequestSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cargoRequestServiceStub.update.calledWith(cargoRequestSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        cargoRequestServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CargoRequestUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cargoRequest = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cargoRequestServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        cargoRequestServiceStub.find.resolves(cargoRequestSample);
        cargoRequestServiceStub.retrieve.resolves([cargoRequestSample]);

        // WHEN
        route = {
          params: {
            cargoRequestId: `${cargoRequestSample.id}`,
          },
        };
        const wrapper = shallowMount(CargoRequestUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.cargoRequest).toMatchObject(cargoRequestSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cargoRequestServiceStub.find.resolves(cargoRequestSample);
        const wrapper = shallowMount(CargoRequestUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
