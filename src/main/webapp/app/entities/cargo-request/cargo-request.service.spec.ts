import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import CargoRequestService from './cargo-request.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { CargoRequest } from '@/shared/model/cargo-request.model';

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
  describe('CargoRequest Service', () => {
    let service: CargoRequestService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new CargoRequestService();
      currentDate = new Date();
      elemDefault = new CargoRequest(123, currentDate, currentDate, 'NEW', false, 0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          createDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          validUntil: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
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

      it('should create a CargoRequest', async () => {
        const returnedFromService = {
          id: 123,
          createDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          validUntil: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { createDate: currentDate, validUntil: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a CargoRequest', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a CargoRequest', async () => {
        const returnedFromService = {
          createDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          validUntil: dayjs(currentDate).format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
          isNegotiable: true,
          budget: 1,
          createdByEncId: 'BBBBBB',
          takenByEncId: 'BBBBBB',
          encId: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { createDate: currentDate, validUntil: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a CargoRequest', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a CargoRequest', async () => {
        const patchObject = {
          createDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          validUntil: dayjs(currentDate).format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
          isNegotiable: true,
          ...new CargoRequest(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { createDate: currentDate, validUntil: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a CargoRequest', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of CargoRequest', async () => {
        const returnedFromService = {
          createDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          validUntil: dayjs(currentDate).format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
          isNegotiable: true,
          budget: 1,
          createdByEncId: 'BBBBBB',
          takenByEncId: 'BBBBBB',
          encId: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { createDate: currentDate, validUntil: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of CargoRequest', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a CargoRequest', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a CargoRequest', async () => {
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
