<template>
  <div>
    <h2 id="page-heading" data-cy="StateProvinceHeading">
      <span v-text="t$('shipperfinderservice4App.stateProvince.home.title')" id="state-province-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.stateProvince.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'StateProvinceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-state-province"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.stateProvince.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.stateProvince.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && stateProvinces && stateProvinces.length === 0">
      <span v-text="t$('shipperfinderservice4App.stateProvince.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="stateProvinces && stateProvinces.length > 0">
      <table class="table table-striped" aria-describedby="stateProvinces">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('shipperfinderservice4App.stateProvince.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('localName')">
              <span v-text="t$('shipperfinderservice4App.stateProvince.localName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'localName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isoCode')">
              <span v-text="t$('shipperfinderservice4App.stateProvince.isoCode')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isoCode'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('country.name')">
              <span v-text="t$('shipperfinderservice4App.stateProvince.country')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'country.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="stateProvince in stateProvinces" :key="stateProvince.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'StateProvinceView', params: { stateProvinceId: stateProvince.id } }">{{
                stateProvince.id
              }}</router-link>
            </td>
            <td>{{ stateProvince.name }}</td>
            <td>{{ stateProvince.localName }}</td>
            <td>{{ stateProvince.isoCode }}</td>
            <td>
              <div v-if="stateProvince.country">
                <router-link :to="{ name: 'CountryView', params: { countryId: stateProvince.country.id } }">{{
                  stateProvince.country.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'StateProvinceView', params: { stateProvinceId: stateProvince.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'StateProvinceEdit', params: { stateProvinceId: stateProvince.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(stateProvince)"
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
          id="shipperfinderservice4App.stateProvince.delete.question"
          data-cy="stateProvinceDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-stateProvince-heading"
          v-text="t$('shipperfinderservice4App.stateProvince.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-stateProvince"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeStateProvince()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./state-province.component.ts"></script>
