<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.cargoMsg.home.createOrEditLabel"
          data-cy="CargoMsgCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.cargoMsg.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="cargoMsg.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="cargoMsg.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.cargoMsg.msg')" for="cargo-msg-msg"></label>
            <input
              type="text"
              class="form-control"
              name="msg"
              id="cargo-msg-msg"
              data-cy="msg"
              :class="{ valid: !v$.msg.$invalid, invalid: v$.msg.$invalid }"
              v-model="v$.msg.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoMsg.fromUserEncId')"
              for="cargo-msg-fromUserEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="fromUserEncId"
              id="cargo-msg-fromUserEncId"
              data-cy="fromUserEncId"
              :class="{ valid: !v$.fromUserEncId.$invalid, invalid: v$.fromUserEncId.$invalid }"
              v-model="v$.fromUserEncId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoMsg.toUserEncId')"
              for="cargo-msg-toUserEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="toUserEncId"
              id="cargo-msg-toUserEncId"
              data-cy="toUserEncId"
              :class="{ valid: !v$.toUserEncId.$invalid, invalid: v$.toUserEncId.$invalid }"
              v-model="v$.toUserEncId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoMsg.cargoRequestId')"
              for="cargo-msg-cargoRequestId"
            ></label>
            <input
              type="number"
              class="form-control"
              name="cargoRequestId"
              id="cargo-msg-cargoRequestId"
              data-cy="cargoRequestId"
              :class="{ valid: !v$.cargoRequestId.$invalid, invalid: v$.cargoRequestId.$invalid }"
              v-model.number="v$.cargoRequestId.$model"
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
<script lang="ts" src="./cargo-msg-update.component.ts"></script>
