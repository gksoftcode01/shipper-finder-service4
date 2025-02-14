<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.country.home.createOrEditLabel"
          data-cy="CountryCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.country.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="country.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="country.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.country.name')" for="country-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="country-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.country.localName')" for="country-localName"></label>
            <input
              type="text"
              class="form-control"
              name="localName"
              id="country-localName"
              data-cy="localName"
              :class="{ valid: !v$.localName.$invalid, invalid: v$.localName.$invalid }"
              v-model="v$.localName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.country.iso2')" for="country-iso2"></label>
            <input
              type="text"
              class="form-control"
              name="iso2"
              id="country-iso2"
              data-cy="iso2"
              :class="{ valid: !v$.iso2.$invalid, invalid: v$.iso2.$invalid }"
              v-model="v$.iso2.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.country.iso3')" for="country-iso3"></label>
            <input
              type="text"
              class="form-control"
              name="iso3"
              id="country-iso3"
              data-cy="iso3"
              :class="{ valid: !v$.iso3.$invalid, invalid: v$.iso3.$invalid }"
              v-model="v$.iso3.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.country.numericCode')" for="country-numericCode"></label>
            <input
              type="text"
              class="form-control"
              name="numericCode"
              id="country-numericCode"
              data-cy="numericCode"
              :class="{ valid: !v$.numericCode.$invalid, invalid: v$.numericCode.$invalid }"
              v-model="v$.numericCode.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.country.phoneCode')" for="country-phoneCode"></label>
            <input
              type="text"
              class="form-control"
              name="phoneCode"
              id="country-phoneCode"
              data-cy="phoneCode"
              :class="{ valid: !v$.phoneCode.$invalid, invalid: v$.phoneCode.$invalid }"
              v-model="v$.phoneCode.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.country.currency')" for="country-currency"></label>
            <input
              type="text"
              class="form-control"
              name="currency"
              id="country-currency"
              data-cy="currency"
              :class="{ valid: !v$.currency.$invalid, invalid: v$.currency.$invalid }"
              v-model="v$.currency.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.country.currencyName')"
              for="country-currencyName"
            ></label>
            <input
              type="text"
              class="form-control"
              name="currencyName"
              id="country-currencyName"
              data-cy="currencyName"
              :class="{ valid: !v$.currencyName.$invalid, invalid: v$.currencyName.$invalid }"
              v-model="v$.currencyName.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.country.currencySymbol')"
              for="country-currencySymbol"
            ></label>
            <input
              type="text"
              class="form-control"
              name="currencySymbol"
              id="country-currencySymbol"
              data-cy="currencySymbol"
              :class="{ valid: !v$.currencySymbol.$invalid, invalid: v$.currencySymbol.$invalid }"
              v-model="v$.currencySymbol.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.country.emoji')" for="country-emoji"></label>
            <input
              type="text"
              class="form-control"
              name="emoji"
              id="country-emoji"
              data-cy="emoji"
              :class="{ valid: !v$.emoji.$invalid, invalid: v$.emoji.$invalid }"
              v-model="v$.emoji.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.country.emojiU')" for="country-emojiU"></label>
            <input
              type="text"
              class="form-control"
              name="emojiU"
              id="country-emojiU"
              data-cy="emojiU"
              :class="{ valid: !v$.emojiU.$invalid, invalid: v$.emojiU.$invalid }"
              v-model="v$.emojiU.$model"
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
<script lang="ts" src="./country-update.component.ts"></script>
