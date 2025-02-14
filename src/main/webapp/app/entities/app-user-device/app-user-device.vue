<template>
  <div>
    <h2 id="page-heading" data-cy="AppUserDeviceHeading">
      <span v-text="t$('shipperfinderservice4App.appUserDevice.home.title')" id="app-user-device-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.appUserDevice.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'AppUserDeviceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-app-user-device"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.appUserDevice.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.appUserDevice.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && appUserDevices && appUserDevices.length === 0">
      <span v-text="t$('shipperfinderservice4App.appUserDevice.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="appUserDevices && appUserDevices.length > 0">
      <table class="table table-striped" aria-describedby="appUserDevices">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('deviceCode')">
              <span v-text="t$('shipperfinderservice4App.appUserDevice.deviceCode')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'deviceCode'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('notificationToken')">
              <span v-text="t$('shipperfinderservice4App.appUserDevice.notificationToken')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notificationToken'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('lastLogin')">
              <span v-text="t$('shipperfinderservice4App.appUserDevice.lastLogin')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastLogin'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('active')">
              <span v-text="t$('shipperfinderservice4App.appUserDevice.active')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'active'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('userEncId')">
              <span v-text="t$('shipperfinderservice4App.appUserDevice.userEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'userEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="appUserDevice in appUserDevices" :key="appUserDevice.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AppUserDeviceView', params: { appUserDeviceId: appUserDevice.id } }">{{
                appUserDevice.id
              }}</router-link>
            </td>
            <td>{{ appUserDevice.deviceCode }}</td>
            <td>{{ appUserDevice.notificationToken }}</td>
            <td>{{ formatDateShort(appUserDevice.lastLogin) || '' }}</td>
            <td>{{ appUserDevice.active }}</td>
            <td>{{ appUserDevice.userEncId }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'AppUserDeviceView', params: { appUserDeviceId: appUserDevice.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'AppUserDeviceEdit', params: { appUserDeviceId: appUserDevice.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(appUserDevice)"
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
          id="shipperfinderservice4App.appUserDevice.delete.question"
          data-cy="appUserDeviceDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-appUserDevice-heading"
          v-text="t$('shipperfinderservice4App.appUserDevice.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-appUserDevice"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeAppUserDevice()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./app-user-device.component.ts"></script>
