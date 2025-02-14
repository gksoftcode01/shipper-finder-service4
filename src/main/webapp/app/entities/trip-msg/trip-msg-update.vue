<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.tripMsg.home.createOrEditLabel"
          data-cy="TripMsgCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.tripMsg.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="tripMsg.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="tripMsg.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tripMsg.msg')" for="trip-msg-msg"></label>
            <input
              type="text"
              class="form-control"
              name="msg"
              id="trip-msg-msg"
              data-cy="msg"
              :class="{ valid: !v$.msg.$invalid, invalid: v$.msg.$invalid }"
              v-model="v$.msg.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.tripMsg.fromUserEncId')"
              for="trip-msg-fromUserEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="fromUserEncId"
              id="trip-msg-fromUserEncId"
              data-cy="fromUserEncId"
              :class="{ valid: !v$.fromUserEncId.$invalid, invalid: v$.fromUserEncId.$invalid }"
              v-model="v$.fromUserEncId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.tripMsg.toUserEncId')"
              for="trip-msg-toUserEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="toUserEncId"
              id="trip-msg-toUserEncId"
              data-cy="toUserEncId"
              :class="{ valid: !v$.toUserEncId.$invalid, invalid: v$.toUserEncId.$invalid }"
              v-model="v$.toUserEncId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tripMsg.tripId')" for="trip-msg-tripId"></label>
            <input
              type="number"
              class="form-control"
              name="tripId"
              id="trip-msg-tripId"
              data-cy="tripId"
              :class="{ valid: !v$.tripId.$invalid, invalid: v$.tripId.$invalid }"
              v-model.number="v$.tripId.$model"
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
<script lang="ts" src="./trip-msg-update.component.ts"></script>
