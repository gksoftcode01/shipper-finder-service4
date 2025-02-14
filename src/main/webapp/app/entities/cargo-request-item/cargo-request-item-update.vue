<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.cargoRequestItem.home.createOrEditLabel"
          data-cy="CargoRequestItemCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.cargoRequestItem.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="cargoRequestItem.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="cargoRequestItem.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequestItem.maxQty')"
              for="cargo-request-item-maxQty"
            ></label>
            <input
              type="number"
              class="form-control"
              name="maxQty"
              id="cargo-request-item-maxQty"
              data-cy="maxQty"
              :class="{ valid: !v$.maxQty.$invalid, invalid: v$.maxQty.$invalid }"
              v-model.number="v$.maxQty.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequestItem.photoUrl')"
              for="cargo-request-item-photoUrl"
            ></label>
            <input
              type="text"
              class="form-control"
              name="photoUrl"
              id="cargo-request-item-photoUrl"
              data-cy="photoUrl"
              :class="{ valid: !v$.photoUrl.$invalid, invalid: v$.photoUrl.$invalid }"
              v-model="v$.photoUrl.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequestItem.item')"
              for="cargo-request-item-item"
            ></label>
            <select class="form-control" id="cargo-request-item-item" data-cy="item" name="item" v-model="cargoRequestItem.item">
              <option :value="null"></option>
              <option
                :value="cargoRequestItem.item && itemOption.id === cargoRequestItem.item.id ? cargoRequestItem.item : itemOption"
                v-for="itemOption in items"
                :key="itemOption.id"
              >
                {{ itemOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequestItem.unit')"
              for="cargo-request-item-unit"
            ></label>
            <select class="form-control" id="cargo-request-item-unit" data-cy="unit" name="unit" v-model="cargoRequestItem.unit">
              <option :value="null"></option>
              <option
                :value="cargoRequestItem.unit && unitOption.id === cargoRequestItem.unit.id ? cargoRequestItem.unit : unitOption"
                v-for="unitOption in units"
                :key="unitOption.id"
              >
                {{ unitOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('shipperfinderservice4App.cargoRequestItem.tag')" for="cargo-request-item-tag"></label>
            <select
              class="form-control"
              id="cargo-request-item-tags"
              data-cy="tag"
              multiple
              name="tag"
              v-if="cargoRequestItem.tags !== undefined"
              v-model="cargoRequestItem.tags"
            >
              <option :value="getSelected(cargoRequestItem.tags, tagOption, 'id')" v-for="tagOption in tags" :key="tagOption.id">
                {{ tagOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.cargoRequestItem.cargoRequest')"
              for="cargo-request-item-cargoRequest"
            ></label>
            <select
              class="form-control"
              id="cargo-request-item-cargoRequest"
              data-cy="cargoRequest"
              name="cargoRequest"
              v-model="cargoRequestItem.cargoRequest"
            >
              <option :value="null"></option>
              <option
                :value="
                  cargoRequestItem.cargoRequest && cargoRequestOption.id === cargoRequestItem.cargoRequest.id
                    ? cargoRequestItem.cargoRequest
                    : cargoRequestOption
                "
                v-for="cargoRequestOption in cargoRequests"
                :key="cargoRequestOption.id"
              >
                {{ cargoRequestOption.id }}
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
<script lang="ts" src="./cargo-request-item-update.component.ts"></script>
