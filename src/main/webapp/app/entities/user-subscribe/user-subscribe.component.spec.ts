import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import UserSubscribe from './user-subscribe.vue';
import UserSubscribeService from './user-subscribe.service';
import AlertService from '@/shared/alert/alert.service';

type UserSubscribeComponentType = InstanceType<typeof UserSubscribe>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('UserSubscribe Management Component', () => {
    let userSubscribeServiceStub: SinonStubbedInstance<UserSubscribeService>;
    let mountOptions: MountingOptions<UserSubscribeComponentType>['global'];

    beforeEach(() => {
      userSubscribeServiceStub = sinon.createStubInstance<UserSubscribeService>(UserSubscribeService);
      userSubscribeServiceStub.retrieve.resolves({ headers: {} });

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
          userSubscribeService: () => userSubscribeServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        userSubscribeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(UserSubscribe, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(userSubscribeServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.userSubscribes[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(UserSubscribe, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(userSubscribeServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: UserSubscribeComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(UserSubscribe, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        userSubscribeServiceStub.retrieve.reset();
        userSubscribeServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        userSubscribeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(userSubscribeServiceStub.retrieve.called).toBeTruthy();
        expect(comp.userSubscribes[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        userSubscribeServiceStub.retrieve.reset();
        userSubscribeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(userSubscribeServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.userSubscribes[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(userSubscribeServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        userSubscribeServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeUserSubscribe();
        await comp.$nextTick(); // clear components

        // THEN
        expect(userSubscribeServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(userSubscribeServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
