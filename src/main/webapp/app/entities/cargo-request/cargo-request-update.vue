<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.cargoRequest.home.createOrEditLabel"
          data-cy="CargoRequestCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.cargoRequest.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="cargoRequest.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="cargoRequest.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.createDate')"
              for="cargo-request-createDate"
            ></label>
            <div class="d-flex">
              <input
                id="cargo-request-createDate"
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
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.validUntil')"
              for="cargo-request-validUntil"
            ></label>
            <div class="d-flex">
              <input
                id="cargo-request-validUntil"
                data-cy="validUntil"
                type="datetime-local"
                class="form-control"
                name="validUntil"
                :class="{ valid: !v$.validUntil.$invalid, invalid: v$.validUntil.$invalid }"
                :value="convertDateTimeFromServer(v$.validUntil.$model)"
                @change="updateInstantField('validUntil', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.status')"
              for="cargo-request-status"
            ></label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="cargo-request-status"
              data-cy="status"
            >
              <option
                v-for="cargoRequestStatus in cargoRequestStatusValues"
                :key="cargoRequestStatus"
                :value="cargoRequestStatus"
                :label="t$('shipperfinderservice4App.CargoRequestStatus.' + cargoRequestStatus)"
              >
                {{ cargoRequestStatus }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.isNegotiable')"
              for="cargo-request-isNegotiable"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="isNegotiable"
              id="cargo-request-isNegotiable"
              data-cy="isNegotiable"
              :class="{ valid: !v$.isNegotiable.$invalid, invalid: v$.isNegotiable.$invalid }"
              v-model="v$.isNegotiable.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.budget')"
              for="cargo-request-budget"
            ></label>
            <input
              type="number"
              class="form-control"
              name="budget"
              id="cargo-request-budget"
              data-cy="budget"
              :class="{ valid: !v$.budget.$invalid, invalid: v$.budget.$invalid }"
              v-model.number="v$.budget.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.createdByEncId')"
              for="cargo-request-createdByEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="createdByEncId"
              id="cargo-request-createdByEncId"
              data-cy="createdByEncId"
              :class="{ valid: !v$.createdByEncId.$invalid, invalid: v$.createdByEncId.$invalid }"
              v-model="v$.createdByEncId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.takenByEncId')"
              for="cargo-request-takenByEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="takenByEncId"
              id="cargo-request-takenByEncId"
              data-cy="takenByEncId"
              :class="{ valid: !v$.takenByEncId.$invalid, invalid: v$.takenByEncId.$invalid }"
              v-model="v$.takenByEncId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.cargoRequest.encId')" for="cargo-request-encId"></label>
            <input
              type="text"
              class="form-control"
              name="encId"
              id="cargo-request-encId"
              data-cy="encId"
              :class="{ valid: !v$.encId.$invalid, invalid: v$.encId.$invalid }"
              v-model="v$.encId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.fromCountry')"
              for="cargo-request-fromCountry"
            ></label>
            <select
              class="form-control"
              id="cargo-request-fromCountry"
              data-cy="fromCountry"
              name="fromCountry"
              v-model="cargoRequest.fromCountry"
            >
              <option :value="null"></option>
              <option
                :value="
                  cargoRequest.fromCountry && countryOption.id === cargoRequest.fromCountry.id ? cargoRequest.fromCountry : countryOption
                "
                v-for="countryOption in countries"
                :key="countryOption.id"
              >
                {{ countryOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.toCountry')"
              for="cargo-request-toCountry"
            ></label>
            <select class="form-control" id="cargo-request-toCountry" data-cy="toCountry" name="toCountry" v-model="cargoRequest.toCountry">
              <option :value="null"></option>
              <option
                :value="cargoRequest.toCountry && countryOption.id === cargoRequest.toCountry.id ? cargoRequest.toCountry : countryOption"
                v-for="countryOption in countries"
                :key="countryOption.id"
              >
                {{ countryOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.fromState')"
              for="cargo-request-fromState"
            ></label>
            <select class="form-control" id="cargo-request-fromState" data-cy="fromState" name="fromState" v-model="cargoRequest.fromState">
              <option :value="null"></option>
              <option
                :value="
                  cargoRequest.fromState && stateProvinceOption.id === cargoRequest.fromState.id
                    ? cargoRequest.fromState
                    : stateProvinceOption
                "
                v-for="stateProvinceOption in stateProvinces"
                :key="stateProvinceOption.id"
              >
                {{ stateProvinceOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequest.toState')"
              for="cargo-request-toState"
            ></label>
            <select class="form-control" id="cargo-request-toState" data-cy="toState" name="toState" v-model="cargoRequest.toState">
              <option :value="null"></option>
              <option
                :value="
                  cargoRequest.toState && stateProvinceOption.id === cargoRequest.toState.id ? cargoRequest.toState : stateProvinceOption
                "
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
<script lang="ts" src="./cargo-request-update.component.ts"></script>
