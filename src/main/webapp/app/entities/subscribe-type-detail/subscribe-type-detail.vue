<template>
  <div>
    <h2 id="page-heading" data-cy="SubscribeTypeDetailHeading">
      <span v-text="t$('shipperfinderservice4App.subscribeTypeDetail.home.title')" id="subscribe-type-detail-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.subscribeTypeDetail.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SubscribeTypeDetailCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-subscribe-type-detail"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.subscribeTypeDetail.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.subscribeTypeDetail.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && subscribeTypeDetails && subscribeTypeDetails.length === 0">
      <span v-text="t$('shipperfinderservice4App.subscribeTypeDetail.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="subscribeTypeDetails && subscribeTypeDetails.length > 0">
      <table class="table table-striped" aria-describedby="subscribeTypeDetails">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('price')">
              <span v-text="t$('shipperfinderservice4App.subscribeTypeDetail.price')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'price'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxTrip')">
              <span v-text="t$('shipperfinderservice4App.subscribeTypeDetail.maxTrip')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxTrip'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxItems')">
              <span v-text="t$('shipperfinderservice4App.subscribeTypeDetail.maxItems')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxItems'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxRequest')">
              <span v-text="t$('shipperfinderservice4App.subscribeTypeDetail.maxRequest')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxRequest'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxNumberView')">
              <span v-text="t$('shipperfinderservice4App.subscribeTypeDetail.maxNumberView')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxNumberView'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('subscribeType.nameEn')">
              <span v-text="t$('shipperfinderservice4App.subscribeTypeDetail.subscribeType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'subscribeType.nameEn'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="subscribeTypeDetail in subscribeTypeDetails" :key="subscribeTypeDetail.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SubscribeTypeDetailView', params: { subscribeTypeDetailId: subscribeTypeDetail.id } }">{{
                subscribeTypeDetail.id
              }}</router-link>
            </td>
            <td>{{ subscribeTypeDetail.price }}</td>
            <td>{{ subscribeTypeDetail.maxTrip }}</td>
            <td>{{ subscribeTypeDetail.maxItems }}</td>
            <td>{{ subscribeTypeDetail.maxRequest }}</td>
            <td>{{ subscribeTypeDetail.maxNumberView }}</td>
            <td>
              <div v-if="subscribeTypeDetail.subscribeType">
                <router-link :to="{ name: 'SubscribeTypeView', params: { subscribeTypeId: subscribeTypeDetail.subscribeType.id } }">{{
                  subscribeTypeDetail.subscribeType.nameEn
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SubscribeTypeDetailView', params: { subscribeTypeDetailId: subscribeTypeDetail.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SubscribeTypeDetailEdit', params: { subscribeTypeDetailId: subscribeTypeDetail.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(subscribeTypeDetail)"
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
          id="shipperfinderservice4App.subscribeTypeDetail.delete.question"
          data-cy="subscribeTypeDetailDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-subscribeTypeDetail-heading"
          v-text="t$('shipperfinderservice4App.subscribeTypeDetail.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-subscribeTypeDetail"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeSubscribeTypeDetail()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./subscribe-type-detail.component.ts"></script>
