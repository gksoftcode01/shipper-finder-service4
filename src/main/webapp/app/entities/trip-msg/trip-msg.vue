<template>
  <div>
    <h2 id="page-heading" data-cy="TripMsgHeading">
      <span v-text="t$('shipperfinderservice4App.tripMsg.home.title')" id="trip-msg-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.tripMsg.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'TripMsgCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-trip-msg"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.tripMsg.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.tripMsg.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && tripMsgs && tripMsgs.length === 0">
      <span v-text="t$('shipperfinderservice4App.tripMsg.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="tripMsgs && tripMsgs.length > 0">
      <table class="table table-striped" aria-describedby="tripMsgs">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('msg')">
              <span v-text="t$('shipperfinderservice4App.tripMsg.msg')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'msg'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fromUserEncId')">
              <span v-text="t$('shipperfinderservice4App.tripMsg.fromUserEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fromUserEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('toUserEncId')">
              <span v-text="t$('shipperfinderservice4App.tripMsg.toUserEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'toUserEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('tripId')">
              <span v-text="t$('shipperfinderservice4App.tripMsg.tripId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tripId'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tripMsg in tripMsgs" :key="tripMsg.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TripMsgView', params: { tripMsgId: tripMsg.id } }">{{ tripMsg.id }}</router-link>
            </td>
            <td>{{ tripMsg.msg }}</td>
            <td>{{ tripMsg.fromUserEncId }}</td>
            <td>{{ tripMsg.toUserEncId }}</td>
            <td>{{ tripMsg.tripId }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TripMsgView', params: { tripMsgId: tripMsg.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TripMsgEdit', params: { tripMsgId: tripMsg.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(tripMsg)"
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
          id="shipperfinderservice4App.tripMsg.delete.question"
          data-cy="tripMsgDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-tripMsg-heading" v-text="t$('shipperfinderservice4App.tripMsg.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-tripMsg"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeTripMsg()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./trip-msg.component.ts"></script>
