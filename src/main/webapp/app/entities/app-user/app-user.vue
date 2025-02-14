<template>
  <div>
    <h2 id="page-heading" data-cy="AppUserHeading">
      <span v-text="t$('shipperfinderservice4App.appUser.home.title')" id="app-user-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.appUser.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'AppUserCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-app-user"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.appUser.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.appUser.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && appUsers && appUsers.length === 0">
      <span v-text="t$('shipperfinderservice4App.appUser.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="appUsers && appUsers.length > 0">
      <table class="table table-striped" aria-describedby="appUsers">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('birthDate')">
              <span v-text="t$('shipperfinderservice4App.appUser.birthDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'birthDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('gender')">
              <span v-text="t$('shipperfinderservice4App.appUser.gender')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'gender'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('registerDate')">
              <span v-text="t$('shipperfinderservice4App.appUser.registerDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'registerDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('phoneNumber')">
              <span v-text="t$('shipperfinderservice4App.appUser.phoneNumber')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'phoneNumber'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('mobileNumber')">
              <span v-text="t$('shipperfinderservice4App.appUser.mobileNumber')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'mobileNumber'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fullName')">
              <span v-text="t$('shipperfinderservice4App.appUser.fullName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fullName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isVerified')">
              <span v-text="t$('shipperfinderservice4App.appUser.isVerified')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isVerified'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('userId')">
              <span v-text="t$('shipperfinderservice4App.appUser.userId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'userId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('firstName')">
              <span v-text="t$('shipperfinderservice4App.appUser.firstName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'firstName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('lastName')">
              <span v-text="t$('shipperfinderservice4App.appUser.lastName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('encId')">
              <span v-text="t$('shipperfinderservice4App.appUser.encId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'encId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('preferdLanguage.name')">
              <span v-text="t$('shipperfinderservice4App.appUser.preferdLanguage')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'preferdLanguage.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('location.name')">
              <span v-text="t$('shipperfinderservice4App.appUser.location')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'location.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="appUser in appUsers" :key="appUser.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AppUserView', params: { appUserId: appUser.id } }">{{ appUser.id }}</router-link>
            </td>
            <td>{{ formatDateShort(appUser.birthDate) || '' }}</td>
            <td v-text="t$('shipperfinderservice4App.GenderType.' + appUser.gender)"></td>
            <td>{{ formatDateShort(appUser.registerDate) || '' }}</td>
            <td>{{ appUser.phoneNumber }}</td>
            <td>{{ appUser.mobileNumber }}</td>
            <td>{{ appUser.fullName }}</td>
            <td>{{ appUser.isVerified }}</td>
            <td>{{ appUser.userId }}</td>
            <td>{{ appUser.firstName }}</td>
            <td>{{ appUser.lastName }}</td>
            <td>{{ appUser.encId }}</td>
            <td>
              <div v-if="appUser.preferdLanguage">
                <router-link :to="{ name: 'LanguagesView', params: { languagesId: appUser.preferdLanguage.id } }">{{
                  appUser.preferdLanguage.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="appUser.location">
                <router-link :to="{ name: 'CountryView', params: { countryId: appUser.location.id } }">{{
                  appUser.location.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AppUserView', params: { appUserId: appUser.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AppUserEdit', params: { appUserId: appUser.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(appUser)"
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
          id="shipperfinderservice4App.appUser.delete.question"
          data-cy="appUserDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-appUser-heading" v-text="t$('shipperfinderservice4App.appUser.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-appUser"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeAppUser()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./app-user.component.ts"></script>
