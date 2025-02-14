import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import UserRateService from './user-rate.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { UserRate } from '@/shared/model/user-rate.model';

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
  describe('UserRate Service', () => {
    let service: UserRateService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new UserRateService();
      currentDate = new Date();
      elemDefault = new UserRate(123, 0, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { rateDate: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
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

      it('should create a UserRate', async () => {
        const returnedFromService = { id: 123, rateDate: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { rateDate: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a UserRate', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a UserRate', async () => {
        const returnedFromService = {
          rate: 1,
          note: 'BBBBBB',
          rateDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ratedByEncId: 'BBBBBB',
          ratedEncId: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { rateDate: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a UserRate', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a UserRate', async () => {
        const patchObject = { note: 'BBBBBB', ratedEncId: 'BBBBBB', ...new UserRate() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { rateDate: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a UserRate', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of UserRate', async () => {
        const returnedFromService = {
          rate: 1,
          note: 'BBBBBB',
          rateDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ratedByEncId: 'BBBBBB',
          ratedEncId: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { rateDate: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of UserRate', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a UserRate', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a UserRate', async () => {
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
