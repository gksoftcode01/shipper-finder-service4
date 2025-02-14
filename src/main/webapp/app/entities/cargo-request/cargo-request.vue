<template>
  <div>
    <h2 id="page-heading" data-cy="CargoRequestHeading">
      <span v-text="t$('shipperfinderservice4App.cargoRequest.home.title')" id="cargo-request-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.cargoRequest.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CargoRequestCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-cargo-request"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.cargoRequest.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <div class="row">
      <div class="col-sm-12">
        <form name="searchForm" class="form-inline" @submit.prevent="search(currentSearch)">
          <div class="input-group w-100 mt-3">
            <input
              type="text"
              class="form-control"
              name="currentSearch"
              id="currentSearch"
              :placeholder="t$('shipperfinderservice4App.cargoRequest.home.search')"
              v-model="currentSearch"
            />
            <button type="button" id="launch-search" class="btn btn-primary" @click="search(currentSearch)">
              <font-awesome-icon icon="search"></font-awesome-icon>
            </button>
            <button type="button" id="clear-search" class="btn btn-secondary" @click="clear()" v-if="currentSearch">
              <font-awesome-icon icon="trash"></font-awesome-icon>
            </button>
          </div>
        </form>
      </div>
    </div>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && cargoRequests && cargoRequests.length === 0">
      <span v-text="t$('shipperfinderservice4App.cargoRequest.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="cargoRequests && cargoRequests.length > 0">
      <table class="table table-striped" aria-describedby="cargoRequests">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('createDate')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.createDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('validUntil')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.validUntil')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'validUntil'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.status')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isNegotiable')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.isNegotiable')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isNegotiable'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('budget')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.budget')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'budget'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('createdByEncId')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.createdByEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdByEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('takenByEncId')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.takenByEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'takenByEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('encId')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.encId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'encId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fromCountry.name')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.fromCountry')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fromCountry.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('toCountry.name')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.toCountry')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'toCountry.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fromState.name')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.fromState')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fromState.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('toState.name')">
              <span v-text="t$('shipperfinderservice4App.cargoRequest.toState')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'toState.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cargoRequest in cargoRequests" :key="cargoRequest.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CargoRequestView', params: { cargoRequestId: cargoRequest.id } }">{{
                cargoRequest.id
              }}</router-link>
            </td>
            <td>{{ formatDateShort(cargoRequest.createDate) || '' }}</td>
            <td>{{ formatDateShort(cargoRequest.validUntil) || '' }}</td>
            <td v-text="t$('shipperfinderservice4App.CargoRequestStatus.' + cargoRequest.status)"></td>
            <td>{{ cargoRequest.isNegotiable }}</td>
            <td>{{ cargoRequest.budget }}</td>
            <td>{{ cargoRequest.createdByEncId }}</td>
            <td>{{ cargoRequest.takenByEncId }}</td>
            <td>{{ cargoRequest.encId }}</td>
            <td>
              <div v-if="cargoRequest.fromCountry">
                <router-link :to="{ name: 'CountryView', params: { countryId: cargoRequest.fromCountry.id } }">{{
                  cargoRequest.fromCountry.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="cargoRequest.toCountry">
                <router-link :to="{ name: 'CountryView', params: { countryId: cargoRequest.toCountry.id } }">{{
                  cargoRequest.toCountry.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="cargoRequest.fromState">
                <router-link :to="{ name: 'StateProvinceView', params: { stateProvinceId: cargoRequest.fromState.id } }">{{
                  cargoRequest.fromState.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="cargoRequest.toState">
                <router-link :to="{ name: 'StateProvinceView', params: { stateProvinceId: cargoRequest.toState.id } }">{{
                  cargoRequest.toState.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CargoRequestView', params: { cargoRequestId: cargoRequest.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CargoRequestEdit', params: { cargoRequestId: cargoRequest.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(cargoRequest)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
        <span ref="infiniteScrollEl"></span>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="shipperfinderservice4App.cargoRequest.delete.question"
          data-cy="cargoRequestDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-cargoRequest-heading" v-text="t$('shipperfinderservice4App.cargoRequest.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-cargoRequest"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCargoRequest()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./cargo-request.component.ts"></script>
