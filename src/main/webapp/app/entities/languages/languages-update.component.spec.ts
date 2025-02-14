import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import LanguagesUpdate from './languages-update.vue';
import LanguagesService from './languages.service';
import AlertService from '@/shared/alert/alert.service';

type LanguagesUpdateComponentType = InstanceType<typeof LanguagesUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const languagesSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<LanguagesUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Languages Management Update Component', () => {
    let comp: LanguagesUpdateComponentType;
    let languagesServiceStub: SinonStubbedInstance<LanguagesService>;

    beforeEach(() => {
      route = {};
      languagesServiceStub = sinon.createStubInstance<LanguagesService>(LanguagesService);
      languagesServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          languagesService: () => languagesServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(LanguagesUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.languages = languagesSample;
        languagesServiceStub.update.resolves(languagesSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(languagesServiceStub.update.calledWith(languagesSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        languagesServiceStub.create.resolves(entity);
        const wrapper = shallowMount(LanguagesUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.languages = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(languagesServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        languagesServiceStub.find.resolves(languagesSample);
        languagesServiceStub.retrieve.resolves([languagesSample]);

        // WHEN
        route = {
          params: {
            languagesId: `${languagesSample.id}`,
          },
        };
        const wrapper = shallowMount(LanguagesUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.languages).toMatchObject(languagesSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        languagesServiceStub.find.resolves(languagesSample);
        const wrapper = shallowMount(LanguagesUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
