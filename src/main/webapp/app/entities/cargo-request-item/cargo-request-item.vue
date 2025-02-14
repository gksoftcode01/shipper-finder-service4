<template>
  <div>
    <h2 id="page-heading" data-cy="CargoRequestItemHeading">
      <span v-text="t$('shipperfinderservice4App.cargoRequestItem.home.title')" id="cargo-request-item-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.cargoRequestItem.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CargoRequestItemCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-cargo-request-item"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.cargoRequestItem.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.cargoRequestItem.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && cargoRequestItems && cargoRequestItems.length === 0">
      <span v-text="t$('shipperfinderservice4App.cargoRequestItem.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="cargoRequestItems && cargoRequestItems.length > 0">
      <table class="table table-striped" aria-describedby="cargoRequestItems">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxQty')">
              <span v-text="t$('shipperfinderservice4App.cargoRequestItem.maxQty')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxQty'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('photoUrl')">
              <span v-text="t$('shipperfinderservice4App.cargoRequestItem.photoUrl')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'photoUrl'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('item.name')">
              <span v-text="t$('shipperfinderservice4App.cargoRequestItem.item')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'item.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('unit.name')">
              <span v-text="t$('shipperfinderservice4App.cargoRequestItem.unit')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'unit.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('cargoRequest.id')">
              <span v-text="t$('shipperfinderservice4App.cargoRequestItem.cargoRequest')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cargoRequest.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cargoRequestItem in cargoRequestItems" :key="cargoRequestItem.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CargoRequestItemView', params: { cargoRequestItemId: cargoRequestItem.id } }">{{
                cargoRequestItem.id
              }}</router-link>
            </td>
            <td>{{ cargoRequestItem.maxQty }}</td>
            <td>{{ cargoRequestItem.photoUrl }}</td>
            <td>
              <div v-if="cargoRequestItem.item">
                <router-link :to="{ name: 'ItemView', params: { itemId: cargoRequestItem.item.id } }">{{
                  cargoRequestItem.item.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="cargoRequestItem.unit">
                <router-link :to="{ name: 'UnitView', params: { unitId: cargoRequestItem.unit.id } }">{{
                  cargoRequestItem.unit.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="cargoRequestItem.cargoRequest">
                <router-link :to="{ name: 'CargoRequestView', params: { cargoRequestId: cargoRequestItem.cargoRequest.id } }">{{
                  cargoRequestItem.cargoRequest.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CargoRequestItemView', params: { cargoRequestItemId: cargoRequestItem.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'CargoRequestItemEdit', params: { cargoRequestItemId: cargoRequestItem.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(cargoRequestItem)"
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
          id="shipperfinderservice4App.cargoRequestItem.delete.question"
          data-cy="cargoRequestItemDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-cargoRequestItem-heading"
          v-text="t$('shipperfinderservice4App.cargoRequestItem.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-cargoRequestItem"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCargoRequestItem()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./cargo-request-item.component.ts"></script>
