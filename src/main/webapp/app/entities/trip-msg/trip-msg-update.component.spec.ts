import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TripMsgUpdate from './trip-msg-update.vue';
import TripMsgService from './trip-msg.service';
import AlertService from '@/shared/alert/alert.service';

type TripMsgUpdateComponentType = InstanceType<typeof TripMsgUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tripMsgSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TripMsgUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('TripMsg Management Update Component', () => {
    let comp: TripMsgUpdateComponentType;
    let tripMsgServiceStub: SinonStubbedInstance<TripMsgService>;

    beforeEach(() => {
      route = {};
      tripMsgServiceStub = sinon.createStubInstance<TripMsgService>(TripMsgService);
      tripMsgServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          tripMsgService: () => tripMsgServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TripMsgUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tripMsg = tripMsgSample;
        tripMsgServiceStub.update.resolves(tripMsgSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tripMsgServiceStub.update.calledWith(tripMsgSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        tripMsgServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TripMsgUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tripMsg = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tripMsgServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        tripMsgServiceStub.find.resolves(tripMsgSample);
        tripMsgServiceStub.retrieve.resolves([tripMsgSample]);

        // WHEN
        route = {
          params: {
            tripMsgId: `${tripMsgSample.id}`,
          },
        };
        const wrapper = shallowMount(TripMsgUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.tripMsg).toMatchObject(tripMsgSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tripMsgServiceStub.find.resolves(tripMsgSample);
        const wrapper = shallowMount(TripMsgUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
