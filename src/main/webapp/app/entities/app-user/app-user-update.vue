<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.appUser.home.createOrEditLabel"
          data-cy="AppUserCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.appUser.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="appUser.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="appUser.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.appUser.birthDate')" for="app-user-birthDate"></label>
            <div class="d-flex">
              <input
                id="app-user-birthDate"
                data-cy="birthDate"
                type="datetime-local"
                class="form-control"
                name="birthDate"
                :class="{ valid: !v$.birthDate.$invalid, invalid: v$.birthDate.$invalid }"
                :value="convertDateTimeFromServer(v$.birthDate.$model)"
                @change="updateInstantField('birthDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.appUser.gender')" for="app-user-gender"></label>
            <select
              class="form-control"
              name="gender"
              :class="{ valid: !v$.gender.$invalid, invalid: v$.gender.$invalid }"
              v-model="v$.gender.$model"
              id="app-user-gender"
              data-cy="gender"
            >
              <option
                v-for="genderType in genderTypeValues"
                :key="genderType"
                :value="genderType"
                :label="t$('shipperfinderservice4App.GenderType.' + genderType)"
              >
                {{ genderType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.appUser.registerDate')"
              for="app-user-registerDate"
            ></label>
            <div class="d-flex">
              <input
                id="app-user-registerDate"
                data-cy="registerDate"
                type="datetime-local"
                class="form-control"
                name="registerDate"
                :class="{ valid: !v$.registerDate.$invalid, invalid: v$.registerDate.$invalid }"
                :value="convertDateTimeFromServer(v$.registerDate.$model)"
                @change="updateInstantField('registerDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.appUser.phoneNumber')"
              for="app-user-phoneNumber"
            ></label>
            <input
              type="text"
              class="form-control"
              name="phoneNumber"
              id="app-user-phoneNumber"
              data-cy="phoneNumber"
              :class="{ valid: !v$.phoneNumber.$invalid, invalid: v$.phoneNumber.$invalid }"
              v-model="v$.phoneNumber.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.appUser.mobileNumber')"
              for="app-user-mobileNumber"
            ></label>
            <input
              type="text"
              class="form-control"
              name="mobileNumber"
              id="app-user-mobileNumber"
              data-cy="mobileNumber"
              :class="{ valid: !v$.mobileNumber.$invalid, invalid: v$.mobileNumber.$invalid }"
              v-model="v$.mobileNumber.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.appUser.fullName')" for="app-user-fullName"></label>
            <input
              type="text"
              class="form-control"
              name="fullName"
              id="app-user-fullName"
              data-cy="fullName"
              :class="{ valid: !v$.fullName.$invalid, invalid: v$.fullName.$invalid }"
              v-model="v$.fullName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.appUser.isVerified')" for="app-user-isVerified"></label>
            <input
              type="checkbox"
              class="form-check"
              name="isVerified"
              id="app-user-isVerified"
              data-cy="isVerified"
              :class="{ valid: !v$.isVerified.$invalid, invalid: v$.isVerified.$invalid }"
              v-model="v$.isVerified.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.appUser.userId')" for="app-user-userId"></label>
            <input
              type="number"
              class="form-control"
              name="userId"
              id="app-user-userId"
              data-cy="userId"
              :class="{ valid: !v$.userId.$invalid, invalid: v$.userId.$invalid }"
              v-model.number="v$.userId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.appUser.firstName')" for="app-user-firstName"></label>
            <input
              type="text"
              class="form-control"
              name="firstName"
              id="app-user-firstName"
              data-cy="firstName"
              :class="{ valid: !v$.firstName.$invalid, invalid: v$.firstName.$invalid }"
              v-model="v$.firstName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.appUser.lastName')" for="app-user-lastName"></label>
            <input
              type="text"
              class="form-control"
              name="lastName"
              id="app-user-lastName"
              data-cy="lastName"
              :class="{ valid: !v$.lastName.$invalid, invalid: v$.lastName.$invalid }"
              v-model="v$.lastName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.appUser.encId')" for="app-user-encId"></label>
            <input
              type="text"
              class="form-control"
              name="encId"
              id="app-user-encId"
              data-cy="encId"
              :class="{ valid: !v$.encId.$invalid, invalid: v$.encId.$invalid }"
              v-model="v$.encId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.appUser.preferdLanguage')"
              for="app-user-preferdLanguage"
            ></label>
            <select
              class="form-control"
              id="app-user-preferdLanguage"
              data-cy="preferdLanguage"
              name="preferdLanguage"
              v-model="appUser.preferdLanguage"
            >
              <option :value="null"></option>
              <option
                :value="
                  appUser.preferdLanguage && languagesOption.id === appUser.preferdLanguage.id ? appUser.preferdLanguage : languagesOption
                "
                v-for="languagesOption in languages"
                :key="languagesOption.id"
              >
                {{ languagesOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.appUser.location')" for="app-user-location"></label>
            <select class="form-control" id="app-user-location" data-cy="location" name="location" v-model="appUser.location">
              <option :value="null"></option>
              <option
                :value="appUser.location && countryOption.id === appUser.location.id ? appUser.location : countryOption"
                v-for="countryOption in countries"
                :key="countryOption.id"
              >
                {{ countryOption.name }}
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
<script lang="ts" src="./app-user-update.component.ts"></script>
