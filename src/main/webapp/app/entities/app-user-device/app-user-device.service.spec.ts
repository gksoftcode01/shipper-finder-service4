import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import AppUserDeviceService from './app-user-device.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { AppUserDevice } from '@/shared/model/app-user-device.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('AppUserDevice Service', () => {
    let service: AppUserDeviceService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new AppUserDeviceService();
      currentDate = new Date();
      elemDefault = new AppUserDevice(123, 'AAAAAAA', 'AAAAAAA', currentDate, false, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { lastLogin: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a AppUserDevice', async () => {
        const returnedFromService = { id: 123, lastLogin: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { lastLogin: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a AppUserDevice', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a AppUserDevice', async () => {
        const returnedFromService = {
          deviceCode: 'BBBBBB',
          notificationToken: 'BBBBBB',
          lastLogin: dayjs(currentDate).format(DATE_TIME_FORMAT),
          active: true,
          userEncId: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { lastLogin: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a AppUserDevice', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a AppUserDevice', async () => {
        const patchObject = { deviceCode: 'BBBBBB', ...new AppUserDevice() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { lastLogin: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a AppUserDevice', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of AppUserDevice', async () => {
        const returnedFromService = {
          deviceCode: 'BBBBBB',
          notificationToken: 'BBBBBB',
          lastLogin: dayjs(currentDate).format(DATE_TIME_FORMAT),
          active: true,
          userEncId: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { lastLogin: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of AppUserDevice', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a AppUserDevice', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a AppUserDevice', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
