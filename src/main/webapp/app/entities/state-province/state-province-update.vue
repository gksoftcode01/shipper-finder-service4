<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.stateProvince.home.createOrEditLabel"
          data-cy="StateProvinceCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.stateProvince.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="stateProvince.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="stateProvince.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.stateProvince.name')" for="state-province-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="state-province-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.stateProvince.localName')"
              for="state-province-localName"
            ></label>
            <input
              type="text"
              class="form-control"
              name="localName"
              id="state-province-localName"
              data-cy="localName"
              :class="{ valid: !v$.localName.$invalid, invalid: v$.localName.$invalid }"
              v-model="v$.localName.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.stateProvince.isoCode')"
              for="state-province-isoCode"
            ></label>
            <input
              type="text"
              class="form-control"
              name="isoCode"
              id="state-province-isoCode"
              data-cy="isoCode"
              :class="{ valid: !v$.isoCode.$invalid, invalid: v$.isoCode.$invalid }"
              v-model="v$.isoCode.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.stateProvince.country')"
              for="state-province-country"
            ></label>
            <select class="form-control" id="state-province-country" data-cy="country" name="country" v-model="stateProvince.country">
              <option :value="null"></option>
              <option
                :value="stateProvince.country && countryOption.id === stateProvince.country.id ? stateProvince.country : countryOption"
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
<script lang="ts" src="./state-province-update.component.ts"></script>
