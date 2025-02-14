<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.showNumberHistory.home.createOrEditLabel"
          data-cy="ShowNumberHistoryCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.showNumberHistory.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="showNumberHistory.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="showNumberHistory.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.showNumberHistory.createdDate')"
              for="show-number-history-createdDate"
            ></label>
            <div class="d-flex">
              <input
                id="show-number-history-createdDate"
                data-cy="createdDate"
                type="datetime-local"
                class="form-control"
                name="createdDate"
                :class="{ valid: !v$.createdDate.$invalid, invalid: v$.createdDate.$invalid }"
                :value="convertDateTimeFromServer(v$.createdDate.$model)"
                @change="updateInstantField('createdDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.showNumberHistory.actionByEncId')"
              for="show-number-history-actionByEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="actionByEncId"
              id="show-number-history-actionByEncId"
              data-cy="actionByEncId"
              :class="{ valid: !v$.actionByEncId.$invalid, invalid: v$.actionByEncId.$invalid }"
              v-model="v$.actionByEncId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.showNumberHistory.entityType')"
              for="show-number-history-entityType"
            ></label>
            <input
              type="number"
              class="form-control"
              name="entityType"
              id="show-number-history-entityType"
              data-cy="entityType"
              :class="{ valid: !v$.entityType.$invalid, invalid: v$.entityType.$invalid }"
              v-model.number="v$.entityType.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.showNumberHistory.entityEncId')"
              for="show-number-history-entityEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="entityEncId"
              id="show-number-history-entityEncId"
              data-cy="entityEncId"
              :class="{ valid: !v$.entityEncId.$invalid, invalid: v$.entityEncId.$invalid }"
              v-model="v$.entityEncId.$model"
            />
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
<script lang="ts" src="./show-number-history-update.component.ts"></script>
