import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import ReportAbuseService from './report-abuse.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { ReportAbuse } from '@/shared/model/report-abuse.model';

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
  describe('ReportAbuse Service', () => {
    let service: ReportAbuseService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ReportAbuseService();
      currentDate = new Date();
      elemDefault = new ReportAbuse(123, 'AAAAAAA', 'AAAAAAA', currentDate, 'AAAAAAA', 'NEW');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { reportDate: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
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

      it('should create a ReportAbuse', async () => {
        const returnedFromService = { id: 123, reportDate: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { reportDate: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a ReportAbuse', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a ReportAbuse', async () => {
        const returnedFromService = {
          reportByEncId: 'BBBBBB',
          reportedAgainstEncId: 'BBBBBB',
          reportDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          reportData: 'BBBBBB',
          reportStatus: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { reportDate: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a ReportAbuse', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a ReportAbuse', async () => {
        const patchObject = {
          reportedAgainstEncId: 'BBBBBB',
          reportDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          reportStatus: 'BBBBBB',
          ...new ReportAbuse(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { reportDate: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a ReportAbuse', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of ReportAbuse', async () => {
        const returnedFromService = {
          reportByEncId: 'BBBBBB',
          reportedAgainstEncId: 'BBBBBB',
          reportDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          reportData: 'BBBBBB',
          reportStatus: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { reportDate: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of ReportAbuse', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a ReportAbuse', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a ReportAbuse', async () => {
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
