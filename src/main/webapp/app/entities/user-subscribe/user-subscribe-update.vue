<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.userSubscribe.home.createOrEditLabel"
          data-cy="UserSubscribeCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.userSubscribe.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="userSubscribe.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="userSubscribe.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.userSubscribe.fromDate')"
              for="user-subscribe-fromDate"
            ></label>
            <div class="d-flex">
              <input
                id="user-subscribe-fromDate"
                data-cy="fromDate"
                type="datetime-local"
                class="form-control"
                name="fromDate"
                :class="{ valid: !v$.fromDate.$invalid, invalid: v$.fromDate.$invalid }"
                :value="convertDateTimeFromServer(v$.fromDate.$model)"
                @change="updateInstantField('fromDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.userSubscribe.toDate')"
              for="user-subscribe-toDate"
            ></label>
            <div class="d-flex">
              <input
                id="user-subscribe-toDate"
                data-cy="toDate"
                type="datetime-local"
                class="form-control"
                name="toDate"
                :class="{ valid: !v$.toDate.$invalid, invalid: v$.toDate.$invalid }"
                :value="convertDateTimeFromServer(v$.toDate.$model)"
                @change="updateInstantField('toDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.userSubscribe.isActive')"
              for="user-subscribe-isActive"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="isActive"
              id="user-subscribe-isActive"
              data-cy="isActive"
              :class="{ valid: !v$.isActive.$invalid, invalid: v$.isActive.$invalid }"
              v-model="v$.isActive.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.userSubscribe.subscribedUserEncId')"
              for="user-subscribe-subscribedUserEncId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="subscribedUserEncId"
              id="user-subscribe-subscribedUserEncId"
              data-cy="subscribedUserEncId"
              :class="{ valid: !v$.subscribedUserEncId.$invalid, invalid: v$.subscribedUserEncId.$invalid }"
              v-model="v$.subscribedUserEncId.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.userSubscribe.subscribeType')"
              for="user-subscribe-subscribeType"
            ></label>
            <select
              class="form-control"
              id="user-subscribe-subscribeType"
              data-cy="subscribeType"
              name="subscribeType"
              v-model="userSubscribe.subscribeType"
            >
              <option :value="null"></option>
              <option
                :value="
                  userSubscribe.subscribeType && subscribeTypeOption.id === userSubscribe.subscribeType.id
                    ? userSubscribe.subscribeType
                    : subscribeTypeOption
                "
                v-for="subscribeTypeOption in subscribeTypes"
                :key="subscribeTypeOption.id"
              >
                {{ subscribeTypeOption.type }}
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
<script lang="ts" src="./user-subscribe-update.component.ts"></script>
