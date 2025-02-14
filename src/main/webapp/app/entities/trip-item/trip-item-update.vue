<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.tripItem.home.createOrEditLabel"
          data-cy="TripItemCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.tripItem.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="tripItem.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="tripItem.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tripItem.itemPrice')" for="trip-item-itemPrice"></label>
            <input
              type="number"
              class="form-control"
              name="itemPrice"
              id="trip-item-itemPrice"
              data-cy="itemPrice"
              :class="{ valid: !v$.itemPrice.$invalid, invalid: v$.itemPrice.$invalid }"
              v-model.number="v$.itemPrice.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tripItem.maxQty')" for="trip-item-maxQty"></label>
            <input
              type="number"
              class="form-control"
              name="maxQty"
              id="trip-item-maxQty"
              data-cy="maxQty"
              :class="{ valid: !v$.maxQty.$invalid, invalid: v$.maxQty.$invalid }"
              v-model.number="v$.maxQty.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tripItem.item')" for="trip-item-item"></label>
            <select class="form-control" id="trip-item-item" data-cy="item" name="item" v-model="tripItem.item">
              <option :value="null"></option>
              <option
                :value="tripItem.item && itemOption.id === tripItem.item.id ? tripItem.item : itemOption"
                v-for="itemOption in items"
                :key="itemOption.id"
              >
                {{ itemOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tripItem.unit')" for="trip-item-unit"></label>
            <select class="form-control" id="trip-item-unit" data-cy="unit" name="unit" v-model="tripItem.unit">
              <option :value="null"></option>
              <option
                :value="tripItem.unit && unitOption.id === tripItem.unit.id ? tripItem.unit : unitOption"
                v-for="unitOption in units"
                :key="unitOption.id"
              >
                {{ unitOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('shipperfinderservice4App.tripItem.tag')" for="trip-item-tag"></label>
            <select
              class="form-control"
              id="trip-item-tags"
              data-cy="tag"
              multiple
              name="tag"
              v-if="tripItem.tags !== undefined"
              v-model="tripItem.tags"
            >
              <option :value="getSelected(tripItem.tags, tagOption, 'id')" v-for="tagOption in tags" :key="tagOption.id">
                {{ tagOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tripItem.trip')" for="trip-item-trip"></label>
            <select class="form-control" id="trip-item-trip" data-cy="trip" name="trip" v-model="tripItem.trip">
              <option :value="null"></option>
              <option
                :value="tripItem.trip && tripOption.id === tripItem.trip.id ? tripItem.trip : tripOption"
                v-for="tripOption in trips"
                :key="tripOption.id"
              >
                {{ tripOption.id }}
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
<script lang="ts" src="./trip-item-update.component.ts"></script>
