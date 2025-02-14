import axios from 'axios';
import sinon from 'sinon';

import SubscribeTypeService from './subscribe-type.service';
import { SubscribeType } from '@/shared/model/subscribe-type.model';

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
  describe('SubscribeType Service', () => {
    let service: SubscribeTypeService;
    let elemDefault;

    beforeEach(() => {
      service = new SubscribeTypeService();
      elemDefault = new SubscribeType(
        123,
        'NORMAL',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { ...elemDefault };
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

      it('should create a SubscribeType', async () => {
        const returnedFromService = { id: 123, ...elemDefault };
        const expected = { ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a SubscribeType', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a SubscribeType', async () => {
        const returnedFromService = {
          type: 'BBBBBB',
          nameEn: 'BBBBBB',
          nameAr: 'BBBBBB',
          nameFr: 'BBBBBB',
          nameDe: 'BBBBBB',
          nameUrdu: 'BBBBBB',
          details: 'BBBBBB',
          detailsEn: 'BBBBBB',
          detailsAr: 'BBBBBB',
          detailsFr: 'BBBBBB',
          detailsDe: 'BBBBBB',
          detailsUrdu: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a SubscribeType', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a SubscribeType', async () => {
        const patchObject = { nameEn: 'BBBBBB', nameUrdu: 'BBBBBB', detailsDe: 'BBBBBB', ...new SubscribeType() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a SubscribeType', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of SubscribeType', async () => {
        const returnedFromService = {
          type: 'BBBBBB',
          nameEn: 'BBBBBB',
          nameAr: 'BBBBBB',
          nameFr: 'BBBBBB',
          nameDe: 'BBBBBB',
          nameUrdu: 'BBBBBB',
          details: 'BBBBBB',
          detailsEn: 'BBBBBB',
          detailsAr: 'BBBBBB',
          detailsFr: 'BBBBBB',
          detailsDe: 'BBBBBB',
          detailsUrdu: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of SubscribeType', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a SubscribeType', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a SubscribeType', async () => {
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
