<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.subscribeTypeDetail.home.createOrEditLabel"
          data-cy="SubscribeTypeDetailCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.subscribeTypeDetail.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="subscribeTypeDetail.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="subscribeTypeDetail.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeTypeDetail.price')"
              for="subscribe-type-detail-price"
            ></label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="subscribe-type-detail-price"
              data-cy="price"
              :class="{ valid: !v$.price.$invalid, invalid: v$.price.$invalid }"
              v-model.number="v$.price.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeTypeDetail.maxTrip')"
              for="subscribe-type-detail-maxTrip"
            ></label>
            <input
              type="number"
              class="form-control"
              name="maxTrip"
              id="subscribe-type-detail-maxTrip"
              data-cy="maxTrip"
              :class="{ valid: !v$.maxTrip.$invalid, invalid: v$.maxTrip.$invalid }"
              v-model.number="v$.maxTrip.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeTypeDetail.maxItems')"
              for="subscribe-type-detail-maxItems"
            ></label>
            <input
              type="number"
              class="form-control"
              name="maxItems"
              id="subscribe-type-detail-maxItems"
              data-cy="maxItems"
              :class="{ valid: !v$.maxItems.$invalid, invalid: v$.maxItems.$invalid }"
              v-model.number="v$.maxItems.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeTypeDetail.maxRequest')"
              for="subscribe-type-detail-maxRequest"
            ></label>
            <input
              type="number"
              class="form-control"
              name="maxRequest"
              id="subscribe-type-detail-maxRequest"
              data-cy="maxRequest"
              :class="{ valid: !v$.maxRequest.$invalid, invalid: v$.maxRequest.$invalid }"
              v-model.number="v$.maxRequest.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeTypeDetail.maxNumberView')"
              for="subscribe-type-detail-maxNumberView"
            ></label>
            <input
              type="number"
              class="form-control"
              name="maxNumberView"
              id="subscribe-type-detail-maxNumberView"
              data-cy="maxNumberView"
              :class="{ valid: !v$.maxNumberView.$invalid, invalid: v$.maxNumberView.$invalid }"
              v-model.number="v$.maxNumberView.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeTypeDetail.subscribeType')"
              for="subscribe-type-detail-subscribeType"
            ></label>
            <select
              class="form-control"
              id="subscribe-type-detail-subscribeType"
              data-cy="subscribeType"
              name="subscribeType"
              v-model="subscribeTypeDetail.subscribeType"
            >
              <option :value="null"></option>
              <option
                :value="
                  subscribeTypeDetail.subscribeType && subscribeTypeOption.id === subscribeTypeDetail.subscribeType.id
                    ? subscribeTypeDetail.subscribeType
                    : subscribeTypeOption
                "
                v-for="subscribeTypeOption in subscribeTypes"
                :key="subscribeTypeOption.id"
              >
                {{ subscribeTypeOption.nameEn }}
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
<script lang="ts" src="./subscribe-type-detail-update.component.ts"></script>
