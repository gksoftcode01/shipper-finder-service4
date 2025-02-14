import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import TripUpdate from './trip-update.vue';
import TripService from './trip.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import CountryService from '@/entities/country/country.service';
import StateProvinceService from '@/entities/state-province/state-province.service';

type TripUpdateComponentType = InstanceType<typeof TripUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tripSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TripUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Trip Management Update Component', () => {
    let comp: TripUpdateComponentType;
    let tripServiceStub: SinonStubbedInstance<TripService>;

    beforeEach(() => {
      route = {};
      tripServiceStub = sinon.createStubInstance<TripService>(TripService);
      tripServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          tripService: () => tripServiceStub,
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
        const wrapper = shallowMount(TripUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(TripUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.trip = tripSample;
        tripServiceStub.update.resolves(tripSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tripServiceStub.update.calledWith(tripSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        tripServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TripUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.trip = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tripServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        tripServiceStub.find.resolves(tripSample);
        tripServiceStub.retrieve.resolves([tripSample]);

        // WHEN
        route = {
          params: {
            tripId: `${tripSample.id}`,
          },
        };
        const wrapper = shallowMount(TripUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.trip).toMatchObject(tripSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tripServiceStub.find.resolves(tripSample);
        const wrapper = shallowMount(TripUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
