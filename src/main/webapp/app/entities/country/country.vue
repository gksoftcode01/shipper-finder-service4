<template>
  <div>
    <h2 id="page-heading" data-cy="CountryHeading">
      <span v-text="t$('shipperfinderservice4App.country.home.title')" id="country-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.country.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CountryCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-country"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.country.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.country.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && countries && countries.length === 0">
      <span v-text="t$('shipperfinderservice4App.country.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="countries && countries.length > 0">
      <table class="table table-striped" aria-describedby="countries">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('shipperfinderservice4App.country.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('localName')">
              <span v-text="t$('shipperfinderservice4App.country.localName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'localName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('iso2')">
              <span v-text="t$('shipperfinderservice4App.country.iso2')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'iso2'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('iso3')">
              <span v-text="t$('shipperfinderservice4App.country.iso3')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'iso3'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('numericCode')">
              <span v-text="t$('shipperfinderservice4App.country.numericCode')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numericCode'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('phoneCode')">
              <span v-text="t$('shipperfinderservice4App.country.phoneCode')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'phoneCode'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('currency')">
              <span v-text="t$('shipperfinderservice4App.country.currency')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'currency'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('currencyName')">
              <span v-text="t$('shipperfinderservice4App.country.currencyName')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'currencyName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('currencySymbol')">
              <span v-text="t$('shipperfinderservice4App.country.currencySymbol')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'currencySymbol'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('emoji')">
              <span v-text="t$('shipperfinderservice4App.country.emoji')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'emoji'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('emojiU')">
              <span v-text="t$('shipperfinderservice4App.country.emojiU')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'emojiU'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="country in countries" :key="country.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CountryView', params: { countryId: country.id } }">{{ country.id }}</router-link>
            </td>
            <td>{{ country.name }}</td>
            <td>{{ country.localName }}</td>
            <td>{{ country.iso2 }}</td>
            <td>{{ country.iso3 }}</td>
            <td>{{ country.numericCode }}</td>
            <td>{{ country.phoneCode }}</td>
            <td>{{ country.currency }}</td>
            <td>{{ country.currencyName }}</td>
            <td>{{ country.currencySymbol }}</td>
            <td>{{ country.emoji }}</td>
            <td>{{ country.emojiU }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CountryView', params: { countryId: country.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CountryEdit', params: { countryId: country.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(country)"
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
          id="shipperfinderservice4App.country.delete.question"
          data-cy="countryDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-country-heading" v-text="t$('shipperfinderservice4App.country.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-country"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCountry()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./country.component.ts"></script>
