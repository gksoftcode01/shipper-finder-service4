<template>
  <div>
    <h2 id="page-heading" data-cy="SubscribeTypeHeading">
      <span v-text="t$('shipperfinderservice4App.subscribeType.home.title')" id="subscribe-type-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.subscribeType.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SubscribeTypeCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-subscribe-type"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.subscribeType.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.subscribeType.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && subscribeTypes && subscribeTypes.length === 0">
      <span v-text="t$('shipperfinderservice4App.subscribeType.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="subscribeTypes && subscribeTypes.length > 0">
      <table class="table table-striped" aria-describedby="subscribeTypes">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('type')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.type')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'type'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nameEn')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.nameEn')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameEn'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nameAr')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.nameAr')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameAr'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nameFr')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.nameFr')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameFr'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nameDe')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.nameDe')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameDe'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nameUrdu')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.nameUrdu')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameUrdu'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('details')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.details')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'details'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('detailsEn')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.detailsEn')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'detailsEn'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('detailsAr')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.detailsAr')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'detailsAr'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('detailsFr')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.detailsFr')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'detailsFr'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('detailsDe')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.detailsDe')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'detailsDe'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('detailsUrdu')">
              <span v-text="t$('shipperfinderservice4App.subscribeType.detailsUrdu')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'detailsUrdu'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="subscribeType in subscribeTypes" :key="subscribeType.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SubscribeTypeView', params: { subscribeTypeId: subscribeType.id } }">{{
                subscribeType.id
              }}</router-link>
            </td>
            <td v-text="t$('shipperfinderservice4App.SubscribeTypeEnum.' + subscribeType.type)"></td>
            <td>{{ subscribeType.nameEn }}</td>
            <td>{{ subscribeType.nameAr }}</td>
            <td>{{ subscribeType.nameFr }}</td>
            <td>{{ subscribeType.nameDe }}</td>
            <td>{{ subscribeType.nameUrdu }}</td>
            <td>{{ subscribeType.details }}</td>
            <td>{{ subscribeType.detailsEn }}</td>
            <td>{{ subscribeType.detailsAr }}</td>
            <td>{{ subscribeType.detailsFr }}</td>
            <td>{{ subscribeType.detailsDe }}</td>
            <td>{{ subscribeType.detailsUrdu }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SubscribeTypeView', params: { subscribeTypeId: subscribeType.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SubscribeTypeEdit', params: { subscribeTypeId: subscribeType.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(subscribeType)"
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
          id="shipperfinderservice4App.subscribeType.delete.question"
          data-cy="subscribeTypeDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-subscribeType-heading"
          v-text="t$('shipperfinderservice4App.subscribeType.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-subscribeType"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeSubscribeType()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./subscribe-type.component.ts"></script>
