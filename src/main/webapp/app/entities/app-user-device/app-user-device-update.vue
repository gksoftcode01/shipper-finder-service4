<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.appUserDevice.home.createOrEditLabel"
          data-cy="AppUserDeviceCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.appUserDevice.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="appUserDevice.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="appUserDevice.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.appUserDevice.deviceCode')"
              for="app-user-device-deviceCode"
            ></label>
            <input
              type="text"
              class="form-control"
              name="deviceCode"
              id="app-user-device-deviceCode"
              data-cy="deviceCode"
              :class="{ valid: !v$.deviceCode.$invalid, invalid: v$.deviceCode.$invalid }"
              v-model="v$.deviceCode.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.appUserDevice.notificationToken')"
              for="app-user-device-notificationToken"
            ></label>
            <input
              type="text"
              class="form-control"
              name="notificationToken"
              id="app-user-device-notificationToken"
              data-cy="notificationToken"
              :class="{ valid: !v$.notificationToken.$invalid, invalid: v$.notificationToken.$invalid }"
              v-model="v$.notificationToken.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.appUserDevice.lastLogin')"
              for="app-user-device-lastLogin"
            ></label>
            <div class="d-flex">
              <input
                id="app-user-device-lastLogin"
                data-cy="lastLogin"
                type="datetime-local"
                class="form-control"
                name="lastLogin"
                :class="{ valid: !v$.lastLogin.$invalid, invalid: v$.lastLogin.$invalid }"
                :value="convertDateTimeFromServer(v$.lastLogin.$model)"
                @change="updateInstantField('lastLogin', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.appUserDevice.active')"
              for="app-user-device-active"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="active"
              id="app-user-device-active"
              data-cy="active"
              :class="{ valid: !v$.active.$invalid, invalid: v$.active.$invalid }"
              v-model="v$.active.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.appUserDevice.userEncId')"
              for="app-user-device-userEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="userEncId"
              id="app-user-device-userEncId"
              data-cy="userEncId"
              :class="{ valid: !v$.userEncId.$invalid, invalid: v$.userEncId.$invalid }"
              v-model="v$.userEncId.$model"
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
<script lang="ts" src="./app-user-device-update.component.ts"></script>
