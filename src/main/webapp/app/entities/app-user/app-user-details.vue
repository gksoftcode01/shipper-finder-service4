<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="appUser">
        <h2 class="jh-entity-heading" data-cy="appUserDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.appUser.detail.title')"></span> {{ appUser.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.birthDate')"></span>
          </dt>
          <dd>
            <span v-if="appUser.birthDate">{{ formatDateLong(appUser.birthDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.gender')"></span>
          </dt>
          <dd>
            <span v-text="t$('shipperfinderservice4App.GenderType.' + appUser.gender)"></span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.registerDate')"></span>
          </dt>
          <dd>
            <span v-if="appUser.registerDate">{{ formatDateLong(appUser.registerDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.phoneNumber')"></span>
          </dt>
          <dd>
            <span>{{ appUser.phoneNumber }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.mobileNumber')"></span>
          </dt>
          <dd>
            <span>{{ appUser.mobileNumber }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.fullName')"></span>
          </dt>
          <dd>
            <span>{{ appUser.fullName }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.isVerified')"></span>
          </dt>
          <dd>
            <span>{{ appUser.isVerified }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.userId')"></span>
          </dt>
          <dd>
            <span>{{ appUser.userId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.firstName')"></span>
          </dt>
          <dd>
            <span>{{ appUser.firstName }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.lastName')"></span>
          </dt>
          <dd>
            <span>{{ appUser.lastName }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.encId')"></span>
          </dt>
          <dd>
            <span>{{ appUser.encId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.preferdLanguage')"></span>
          </dt>
          <dd>
            <div v-if="appUser.preferdLanguage">
              <router-link :to="{ name: 'LanguagesView', params: { languagesId: appUser.preferdLanguage.id } }">{{
                appUser.preferdLanguage.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.appUser.location')"></span>
          </dt>
          <dd>
            <div v-if="appUser.location">
              <router-link :to="{ name: 'CountryView', params: { countryId: appUser.location.id } }">{{
                appUser.location.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="appUser.id" :to="{ name: 'AppUserEdit', params: { appUserId: appUser.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./app-user-details.component.ts"></script>
