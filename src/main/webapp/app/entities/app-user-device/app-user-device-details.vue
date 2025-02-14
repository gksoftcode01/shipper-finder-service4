<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="appUserDevice">
        <h2 class="jh-entity-heading" data-cy="appUserDeviceDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.appUserDevice.detail.title')"></span> {{ appUserDevice.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUserDevice.deviceCode')"></span>
          </dt>
          <dd>
            <span>{{ appUserDevice.deviceCode }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUserDevice.notificationToken')"></span>
          </dt>
          <dd>
            <span>{{ appUserDevice.notificationToken }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUserDevice.lastLogin')"></span>
          </dt>
          <dd>
            <span v-if="appUserDevice.lastLogin">{{ formatDateLong(appUserDevice.lastLogin) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUserDevice.active')"></span>
          </dt>
          <dd>
            <span>{{ appUserDevice.active }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUserDevice.userEncId')"></span>
          </dt>
          <dd>
            <span>{{ appUserDevice.userEncId }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="appUserDevice.id"
          :to="{ name: 'AppUserDeviceEdit', params: { appUserDeviceId: appUserDevice.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./app-user-device-details.component.ts"></script>
