import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Languages from './languages.vue';
import LanguagesService from './languages.service';
import AlertService from '@/shared/alert/alert.service';

type LanguagesComponentType = InstanceType<typeof Languages>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Languages Management Component', () => {
    let languagesServiceStub: SinonStubbedInstance<LanguagesService>;
    let mountOptions: MountingOptions<LanguagesComponentType>['global'];

    beforeEach(() => {
      languagesServiceStub = sinon.createStubInstance<LanguagesService>(LanguagesService);
      languagesServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          languagesService: () => languagesServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        languagesServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Languages, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(languagesServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.languages[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(Languages, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(languagesServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: LanguagesComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Languages, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        languagesServiceStub.retrieve.reset();
        languagesServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        languagesServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(languagesServiceStub.retrieve.called).toBeTruthy();
        expect(comp.languages[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        languagesServiceStub.retrieve.reset();
        languagesServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(languagesServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.languages[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(languagesServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        languagesServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeLanguages();
        await comp.$nextTick(); // clear components

        // THEN
        expect(languagesServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(languagesServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
