<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.trip.home.createOrEditLabel"
          data-cy="TripCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.trip.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="trip.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="trip.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.tripDate')" for="trip-tripDate"></label>
            <div class="d-flex">
              <input
                id="trip-tripDate"
                data-cy="tripDate"
                type="datetime-local"
                class="form-control"
                name="tripDate"
                :class="{ valid: !v$.tripDate.$invalid, invalid: v$.tripDate.$invalid }"
                :value="convertDateTimeFromServer(v$.tripDate.$model)"
                @change="updateInstantField('tripDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.arriveDate')" for="trip-arriveDate"></label>
            <div class="d-flex">
              <input
                id="trip-arriveDate"
                data-cy="arriveDate"
                type="datetime-local"
                class="form-control"
                name="arriveDate"
                :class="{ valid: !v$.arriveDate.$invalid, invalid: v$.arriveDate.$invalid }"
                :value="convertDateTimeFromServer(v$.arriveDate.$model)"
                @change="updateInstantField('arriveDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.maxWeight')" for="trip-maxWeight"></label>
            <input
              type="number"
              class="form-control"
              name="maxWeight"
              id="trip-maxWeight"
              data-cy="maxWeight"
              :class="{ valid: !v$.maxWeight.$invalid, invalid: v$.maxWeight.$invalid }"
              v-model.number="v$.maxWeight.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.notes')" for="trip-notes"></label>
            <input
              type="text"
              class="form-control"
              name="notes"
              id="trip-notes"
              data-cy="notes"
              :class="{ valid: !v$.notes.$invalid, invalid: v$.notes.$invalid }"
              v-model="v$.notes.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.createDate')" for="trip-createDate"></label>
            <div class="d-flex">
              <input
                id="trip-createDate"
                data-cy="createDate"
                type="datetime-local"
                class="form-control"
                name="createDate"
                :class="{ valid: !v$.createDate.$invalid, invalid: v$.createDate.$invalid }"
                :value="convertDateTimeFromServer(v$.createDate.$model)"
                @change="updateInstantField('createDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.isNegotiate')" for="trip-isNegotiate"></label>
            <input
              type="checkbox"
              class="form-check"
              name="isNegotiate"
              id="trip-isNegotiate"
              data-cy="isNegotiate"
              :class="{ valid: !v$.isNegotiate.$invalid, invalid: v$.isNegotiate.$invalid }"
              v-model="v$.isNegotiate.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.status')" for="trip-status"></label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="trip-status"
              data-cy="status"
            >
              <option
                v-for="tripStatus in tripStatusValues"
                :key="tripStatus"
                :value="tripStatus"
                :label="t$('shipperfinderservice4App.TripStatus.' + tripStatus)"
              >
                {{ tripStatus }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.createdByEncId')" for="trip-createdByEncId"></label>
            <input
              type="text"
              class="form-control"
              name="createdByEncId"
              id="trip-createdByEncId"
              data-cy="createdByEncId"
              :class="{ valid: !v$.createdByEncId.$invalid, invalid: v$.createdByEncId.$invalid }"
              v-model="v$.createdByEncId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.encId')" for="trip-encId"></label>
            <input
              type="text"
              class="form-control"
              name="encId"
              id="trip-encId"
              data-cy="encId"
              :class="{ valid: !v$.encId.$invalid, invalid: v$.encId.$invalid }"
              v-model="v$.encId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.fromCountry')" for="trip-fromCountry"></label>
            <select class="form-control" id="trip-fromCountry" data-cy="fromCountry" name="fromCountry" v-model="trip.fromCountry">
              <option :value="null"></option>
              <option
                :value="trip.fromCountry && countryOption.id === trip.fromCountry.id ? trip.fromCountry : countryOption"
                v-for="countryOption in countries"
                :key="countryOption.id"
              >
                {{ countryOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.toCountry')" for="trip-toCountry"></label>
            <select class="form-control" id="trip-toCountry" data-cy="toCountry" name="toCountry" v-model="trip.toCountry">
              <option :value="null"></option>
              <option
                :value="trip.toCountry && countryOption.id === trip.toCountry.id ? trip.toCountry : countryOption"
                v-for="countryOption in countries"
                :key="countryOption.id"
              >
                {{ countryOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.fromState')" for="trip-fromState"></label>
            <select class="form-control" id="trip-fromState" data-cy="fromState" name="fromState" v-model="trip.fromState">
              <option :value="null"></option>
              <option
                :value="trip.fromState && stateProvinceOption.id === trip.fromState.id ? trip.fromState : stateProvinceOption"
                v-for="stateProvinceOption in stateProvinces"
                :key="stateProvinceOption.id"
              >
                {{ stateProvinceOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.trip.toState')" for="trip-toState"></label>
            <select class="form-control" id="trip-toState" data-cy="toState" name="toState" v-model="trip.toState">
              <option :value="null"></option>
              <option
                :value="trip.toState && stateProvinceOption.id === trip.toState.id ? trip.toState : stateProvinceOption"
                v-for="stateProvinceOption in stateProvinces"
                :key="stateProvinceOption.id"
              >
                {{ stateProvinceOption.name }}
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
<script lang="ts" src="./trip-update.component.ts"></script>
