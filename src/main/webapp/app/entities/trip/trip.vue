<template>
  <div>
    <h2 id="page-heading" data-cy="TripHeading">
      <span v-text="t$('shipperfinderservice4App.trip.home.title')" id="trip-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.trip.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'TripCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-trip">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.trip.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.trip.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && trips && trips.length === 0">
      <span v-text="t$('shipperfinderservice4App.trip.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="trips && trips.length > 0">
      <table class="table table-striped" aria-describedby="trips">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('tripDate')">
              <span v-text="t$('shipperfinderservice4App.trip.tripDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tripDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('arriveDate')">
              <span v-text="t$('shipperfinderservice4App.trip.arriveDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'arriveDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxWeight')">
              <span v-text="t$('shipperfinderservice4App.trip.maxWeight')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxWeight'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('notes')">
              <span v-text="t$('shipperfinderservice4App.trip.notes')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notes'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('createDate')">
              <span v-text="t$('shipperfinderservice4App.trip.createDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isNegotiate')">
              <span v-text="t$('shipperfinderservice4App.trip.isNegotiate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isNegotiate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status')">
              <span v-text="t$('shipperfinderservice4App.trip.status')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('createdByEncId')">
              <span v-text="t$('shipperfinderservice4App.trip.createdByEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdByEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('encId')">
              <span v-text="t$('shipperfinderservice4App.trip.encId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'encId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fromCountry.name')">
              <span v-text="t$('shipperfinderservice4App.trip.fromCountry')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fromCountry.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('toCountry.name')">
              <span v-text="t$('shipperfinderservice4App.trip.toCountry')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'toCountry.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fromState.name')">
              <span v-text="t$('shipperfinderservice4App.trip.fromState')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fromState.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('toState.name')">
              <span v-text="t$('shipperfinderservice4App.trip.toState')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'toState.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="trip in trips" :key="trip.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TripView', params: { tripId: trip.id } }">{{ trip.id }}</router-link>
            </td>
            <td>{{ formatDateShort(trip.tripDate) || '' }}</td>
            <td>{{ formatDateShort(trip.arriveDate) || '' }}</td>
            <td>{{ trip.maxWeight }}</td>
            <td>{{ trip.notes }}</td>
            <td>{{ formatDateShort(trip.createDate) || '' }}</td>
            <td>{{ trip.isNegotiate }}</td>
            <td v-text="t$('shipperfinderservice4App.TripStatus.' + trip.status)"></td>
            <td>{{ trip.createdByEncId }}</td>
            <td>{{ trip.encId }}</td>
            <td>
              <div v-if="trip.fromCountry">
                <router-link :to="{ name: 'CountryView', params: { countryId: trip.fromCountry.id } }">{{
                  trip.fromCountry.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="trip.toCountry">
                <router-link :to="{ name: 'CountryView', params: { countryId: trip.toCountry.id } }">{{ trip.toCountry.name }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="trip.fromState">
                <router-link :to="{ name: 'StateProvinceView', params: { stateProvinceId: trip.fromState.id } }">{{
                  trip.fromState.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="trip.toState">
                <router-link :to="{ name: 'StateProvinceView', params: { stateProvinceId: trip.toState.id } }">{{
                  trip.toState.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TripView', params: { tripId: trip.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TripEdit', params: { tripId: trip.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(trip)"
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
          id="shipperfinderservice4App.trip.delete.question"
          data-cy="tripDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-trip-heading" v-text="t$('shipperfinderservice4App.trip.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-trip"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeTrip()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./trip.component.ts"></script>
