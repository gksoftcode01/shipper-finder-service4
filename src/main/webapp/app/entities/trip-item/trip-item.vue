<template>
  <div>
    <h2 id="page-heading" data-cy="TripItemHeading">
      <span v-text="t$('shipperfinderservice4App.tripItem.home.title')" id="trip-item-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.tripItem.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'TripItemCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-trip-item"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.tripItem.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.tripItem.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && tripItems && tripItems.length === 0">
      <span v-text="t$('shipperfinderservice4App.tripItem.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="tripItems && tripItems.length > 0">
      <table class="table table-striped" aria-describedby="tripItems">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('itemPrice')">
              <span v-text="t$('shipperfinderservice4App.tripItem.itemPrice')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemPrice'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxQty')">
              <span v-text="t$('shipperfinderservice4App.tripItem.maxQty')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxQty'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('item.name')">
              <span v-text="t$('shipperfinderservice4App.tripItem.item')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'item.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unit.name')">
              <span v-text="t$('shipperfinderservice4App.tripItem.unit')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unit.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('trip.id')">
              <span v-text="t$('shipperfinderservice4App.tripItem.trip')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'trip.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tripItem in tripItems" :key="tripItem.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TripItemView', params: { tripItemId: tripItem.id } }">{{ tripItem.id }}</router-link>
            </td>
            <td>{{ tripItem.itemPrice }}</td>
            <td>{{ tripItem.maxQty }}</td>
            <td>
              <div v-if="tripItem.item">
                <router-link :to="{ name: 'ItemView', params: { itemId: tripItem.item.id } }">{{ tripItem.item.name }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="tripItem.unit">
                <router-link :to="{ name: 'UnitView', params: { unitId: tripItem.unit.id } }">{{ tripItem.unit.name }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="tripItem.trip">
                <router-link :to="{ name: 'TripView', params: { tripId: tripItem.trip.id } }">{{ tripItem.trip.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TripItemView', params: { tripItemId: tripItem.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TripItemEdit', params: { tripItemId: tripItem.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(tripItem)"
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
          id="shipperfinderservice4App.tripItem.delete.question"
          data-cy="tripItemDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-tripItem-heading" v-text="t$('shipperfinderservice4App.tripItem.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-tripItem"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeTripItem()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./trip-item.component.ts"></script>
