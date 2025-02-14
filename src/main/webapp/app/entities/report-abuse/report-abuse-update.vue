<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.reportAbuse.home.createOrEditLabel"
          data-cy="ReportAbuseCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.reportAbuse.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="reportAbuse.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="reportAbuse.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.reportAbuse.reportByEncId')"
              for="report-abuse-reportByEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="reportByEncId"
              id="report-abuse-reportByEncId"
              data-cy="reportByEncId"
              :class="{ valid: !v$.reportByEncId.$invalid, invalid: v$.reportByEncId.$invalid }"
              v-model="v$.reportByEncId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.reportAbuse.reportedAgainstEncId')"
              for="report-abuse-reportedAgainstEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="reportedAgainstEncId"
              id="report-abuse-reportedAgainstEncId"
              data-cy="reportedAgainstEncId"
              :class="{ valid: !v$.reportedAgainstEncId.$invalid, invalid: v$.reportedAgainstEncId.$invalid }"
              v-model="v$.reportedAgainstEncId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.reportAbuse.reportDate')"
              for="report-abuse-reportDate"
            ></label>
            <div class="d-flex">
              <input
                id="report-abuse-reportDate"
                data-cy="reportDate"
                type="datetime-local"
                class="form-control"
                name="reportDate"
                :class="{ valid: !v$.reportDate.$invalid, invalid: v$.reportDate.$invalid }"
                :value="convertDateTimeFromServer(v$.reportDate.$model)"
                @change="updateInstantField('reportDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.reportAbuse.reportData')"
              for="report-abuse-reportData"
            ></label>
            <input
              type="text"
              class="form-control"
              name="reportData"
              id="report-abuse-reportData"
              data-cy="reportData"
              :class="{ valid: !v$.reportData.$invalid, invalid: v$.reportData.$invalid }"
              v-model="v$.reportData.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.reportAbuse.reportStatus')"
              for="report-abuse-reportStatus"
            ></label>
            <select
              class="form-control"
              name="reportStatus"
              :class="{ valid: !v$.reportStatus.$invalid, invalid: v$.reportStatus.$invalid }"
              v-model="v$.reportStatus.$model"
              id="report-abuse-reportStatus"
              data-cy="reportStatus"
            >
              <option
                v-for="reportStatus in reportStatusValues"
                :key="reportStatus"
                :value="reportStatus"
                :label="t$('shipperfinderservice4App.ReportStatus.' + reportStatus)"
              >
                {{ reportStatus }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./report-abuse-update.component.ts"></script>
