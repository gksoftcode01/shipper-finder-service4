<template>
  <div>
    <h2 id="page-heading" data-cy="UserRateHeading">
      <span v-text="t$('shipperfinderservice4App.userRate.home.title')" id="user-rate-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.userRate.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'UserRateCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-user-rate"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.userRate.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.userRate.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && userRates && userRates.length === 0">
      <span v-text="t$('shipperfinderservice4App.userRate.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="userRates && userRates.length > 0">
      <table class="table table-striped" aria-describedby="userRates">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('rate')">
              <span v-text="t$('shipperfinderservice4App.userRate.rate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'rate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('note')">
              <span v-text="t$('shipperfinderservice4App.userRate.note')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'note'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('rateDate')">
              <span v-text="t$('shipperfinderservice4App.userRate.rateDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'rateDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('ratedByEncId')">
              <span v-text="t$('shipperfinderservice4App.userRate.ratedByEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ratedByEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('ratedEncId')">
              <span v-text="t$('shipperfinderservice4App.userRate.ratedEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ratedEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="userRate in userRates" :key="userRate.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'UserRateView', params: { userRateId: userRate.id } }">{{ userRate.id }}</router-link>
            </td>
            <td>{{ userRate.rate }}</td>
            <td>{{ userRate.note }}</td>
            <td>{{ formatDateShort(userRate.rateDate) || '' }}</td>
            <td>{{ userRate.ratedByEncId }}</td>
            <td>{{ userRate.ratedEncId }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'UserRateView', params: { userRateId: userRate.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'UserRateEdit', params: { userRateId: userRate.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(userRate)"
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
          id="shipperfinderservice4App.userRate.delete.question"
          data-cy="userRateDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-userRate-heading" v-text="t$('shipperfinderservice4App.userRate.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-userRate"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeUserRate()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./user-rate.component.ts"></script>
