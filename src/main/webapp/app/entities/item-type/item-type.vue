<template>
  <div>
    <h2 id="page-heading" data-cy="ItemTypeHeading">
      <span v-text="t$('shipperfinderservice4App.itemType.home.title')" id="item-type-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.itemType.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ItemTypeCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-item-type"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.itemType.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.itemType.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && itemTypes && itemTypes.length === 0">
      <span v-text="t$('shipperfinderservice4App.itemType.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="itemTypes && itemTypes.length > 0">
      <table class="table table-striped" aria-describedby="itemTypes">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('shipperfinderservice4App.itemType.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nameEn')">
              <span v-text="t$('shipperfinderservice4App.itemType.nameEn')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameEn'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nameAr')">
              <span v-text="t$('shipperfinderservice4App.itemType.nameAr')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameAr'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nameFr')">
              <span v-text="t$('shipperfinderservice4App.itemType.nameFr')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameFr'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nameDe')">
              <span v-text="t$('shipperfinderservice4App.itemType.nameDe')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameDe'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nameUrdu')">
              <span v-text="t$('shipperfinderservice4App.itemType.nameUrdu')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nameUrdu'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isActive')">
              <span v-text="t$('shipperfinderservice4App.itemType.isActive')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isActive'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="itemType in itemTypes" :key="itemType.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ItemTypeView', params: { itemTypeId: itemType.id } }">{{ itemType.id }}</router-link>
            </td>
            <td>{{ itemType.name }}</td>
            <td>{{ itemType.nameEn }}</td>
            <td>{{ itemType.nameAr }}</td>
            <td>{{ itemType.nameFr }}</td>
            <td>{{ itemType.nameDe }}</td>
            <td>{{ itemType.nameUrdu }}</td>
            <td>{{ itemType.isActive }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ItemTypeView', params: { itemTypeId: itemType.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ItemTypeEdit', params: { itemTypeId: itemType.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(itemType)"
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
          id="shipperfinderservice4App.itemType.delete.question"
          data-cy="itemTypeDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-itemType-heading" v-text="t$('shipperfinderservice4App.itemType.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-itemType"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeItemType()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./item-type.component.ts"></script>
