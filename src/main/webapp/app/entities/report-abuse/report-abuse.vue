<template>
  <div>
    <h2 id="page-heading" data-cy="ReportAbuseHeading">
      <span v-text="t$('shipperfinderservice4App.reportAbuse.home.title')" id="report-abuse-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperfinderservice4App.reportAbuse.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ReportAbuseCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-report-abuse"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperfinderservice4App.reportAbuse.home.createLabel')"></span>
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
              :placeholder="t$('shipperfinderservice4App.reportAbuse.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && reportAbuses && reportAbuses.length === 0">
      <span v-text="t$('shipperfinderservice4App.reportAbuse.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="reportAbuses && reportAbuses.length > 0">
      <table class="table table-striped" aria-describedby="reportAbuses">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('reportByEncId')">
              <span v-text="t$('shipperfinderservice4App.reportAbuse.reportByEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reportByEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('reportedAgainstEncId')">
              <span v-text="t$('shipperfinderservice4App.reportAbuse.reportedAgainstEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reportedAgainstEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('reportDate')">
              <span v-text="t$('shipperfinderservice4App.reportAbuse.reportDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reportDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('reportData')">
              <span v-text="t$('shipperfinderservice4App.reportAbuse.reportData')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reportData'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('reportStatus')">
              <span v-text="t$('shipperfinderservice4App.reportAbuse.reportStatus')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'reportStatus'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="reportAbuse in reportAbuses" :key="reportAbuse.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ReportAbuseView', params: { reportAbuseId: reportAbuse.id } }">{{ reportAbuse.id }}</router-link>
            </td>
            <td>{{ reportAbuse.reportByEncId }}</td>
            <td>{{ reportAbuse.reportedAgainstEncId }}</td>
            <td>{{ formatDateShort(reportAbuse.reportDate) || '' }}</td>
            <td>{{ reportAbuse.reportData }}</td>
            <td v-text="t$('shipperfinderservice4App.ReportStatus.' + reportAbuse.reportStatus)"></td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ReportAbuseView', params: { reportAbuseId: reportAbuse.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ReportAbuseEdit', params: { reportAbuseId: reportAbuse.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(reportAbuse)"
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
          id="shipperfinderservice4App.reportAbuse.delete.question"
          data-cy="reportAbuseDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-reportAbuse-heading" v-text="t$('shipperfinderservice4App.reportAbuse.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-reportAbuse"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeReportAbuse()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./report-abuse.component.ts"></script>
