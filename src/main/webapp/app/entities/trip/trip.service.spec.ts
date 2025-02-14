import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import TripService from './trip.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Trip } from '@/shared/model/trip.model';

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
  describe('Trip Service', () => {
    let service: TripService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new TripService();
      currentDate = new Date();
      elemDefault = new Trip(123, currentDate, currentDate, 0, 'AAAAAAA', currentDate, false, 'NEW', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          tripDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          arriveDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          createDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a Trip', async () => {
        const returnedFromService = {
          id: 123,
          tripDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          arriveDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          createDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { tripDate: currentDate, arriveDate: currentDate, createDate: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Trip', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Trip', async () => {
        const returnedFromService = {
          tripDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          arriveDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          maxWeight: 1,
          notes: 'BBBBBB',
          createDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          isNegotiate: true,
          status: 'BBBBBB',
          createdByEncId: 'BBBBBB',
          encId: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { tripDate: currentDate, arriveDate: currentDate, createDate: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Trip', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Trip', async () => {
        const patchObject = {
          tripDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          arriveDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          maxWeight: 1,
          notes: 'BBBBBB',
          createdByEncId: 'BBBBBB',
          ...new Trip(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { tripDate: currentDate, arriveDate: currentDate, createDate: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Trip', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Trip', async () => {
        const returnedFromService = {
          tripDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          arriveDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          maxWeight: 1,
          notes: 'BBBBBB',
          createDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          isNegotiate: true,
          status: 'BBBBBB',
          createdByEncId: 'BBBBBB',
          encId: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { tripDate: currentDate, arriveDate: currentDate, createDate: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Trip', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Trip', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Trip', async () => {
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
