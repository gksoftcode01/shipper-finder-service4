<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.userRate.home.createOrEditLabel"
          data-cy="UserRateCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.userRate.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="userRate.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="userRate.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.userRate.rate')" for="user-rate-rate"></label>
            <input
              type="number"
              class="form-control"
              name="rate"
              id="user-rate-rate"
              data-cy="rate"
              :class="{ valid: !v$.rate.$invalid, invalid: v$.rate.$invalid }"
              v-model.number="v$.rate.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.userRate.note')" for="user-rate-note"></label>
            <input
              type="text"
              class="form-control"
              name="note"
              id="user-rate-note"
              data-cy="note"
              :class="{ valid: !v$.note.$invalid, invalid: v$.note.$invalid }"
              v-model="v$.note.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.userRate.rateDate')" for="user-rate-rateDate"></label>
            <div class="d-flex">
              <input
                id="user-rate-rateDate"
                data-cy="rateDate"
                type="datetime-local"
                class="form-control"
                name="rateDate"
                :class="{ valid: !v$.rateDate.$invalid, invalid: v$.rateDate.$invalid }"
                :value="convertDateTimeFromServer(v$.rateDate.$model)"
                @change="updateInstantField('rateDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.userRate.ratedByEncId')"
              for="user-rate-ratedByEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="ratedByEncId"
              id="user-rate-ratedByEncId"
              data-cy="ratedByEncId"
              :class="{ valid: !v$.ratedByEncId.$invalid, invalid: v$.ratedByEncId.$invalid }"
              v-model="v$.ratedByEncId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.userRate.ratedEncId')"
              for="user-rate-ratedEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="ratedEncId"
              id="user-rate-ratedEncId"
              data-cy="ratedEncId"
              :class="{ valid: !v$.ratedEncId.$invalid, invalid: v$.ratedEncId.$invalid }"
              v-model="v$.ratedEncId.$model"
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
<script lang="ts" src="./user-rate-update.component.ts"></script>
