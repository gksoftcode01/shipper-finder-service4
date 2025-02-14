<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.tag.home.createOrEditLabel"
          data-cy="TagCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.tag.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="tag.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="tag.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tag.name')" for="tag-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="tag-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tag.nameEn')" for="tag-nameEn"></label>
            <input
              type="text"
              class="form-control"
              name="nameEn"
              id="tag-nameEn"
              data-cy="nameEn"
              :class="{ valid: !v$.nameEn.$invalid, invalid: v$.nameEn.$invalid }"
              v-model="v$.nameEn.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tag.nameAr')" for="tag-nameAr"></label>
            <input
              type="text"
              class="form-control"
              name="nameAr"
              id="tag-nameAr"
              data-cy="nameAr"
              :class="{ valid: !v$.nameAr.$invalid, invalid: v$.nameAr.$invalid }"
              v-model="v$.nameAr.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tag.nameFr')" for="tag-nameFr"></label>
            <input
              type="text"
              class="form-control"
              name="nameFr"
              id="tag-nameFr"
              data-cy="nameFr"
              :class="{ valid: !v$.nameFr.$invalid, invalid: v$.nameFr.$invalid }"
              v-model="v$.nameFr.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tag.nameDe')" for="tag-nameDe"></label>
            <input
              type="text"
              class="form-control"
              name="nameDe"
              id="tag-nameDe"
              data-cy="nameDe"
              :class="{ valid: !v$.nameDe.$invalid, invalid: v$.nameDe.$invalid }"
              v-model="v$.nameDe.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tag.nameUrdu')" for="tag-nameUrdu"></label>
            <input
              type="text"
              class="form-control"
              name="nameUrdu"
              id="tag-nameUrdu"
              data-cy="nameUrdu"
              :class="{ valid: !v$.nameUrdu.$invalid, invalid: v$.nameUrdu.$invalid }"
              v-model="v$.nameUrdu.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.tag.iconUrl')" for="tag-iconUrl"></label>
            <input
              type="text"
              class="form-control"
              name="iconUrl"
              id="tag-iconUrl"
              data-cy="iconUrl"
              :class="{ valid: !v$.iconUrl.$invalid, invalid: v$.iconUrl.$invalid }"
              v-model="v$.iconUrl.$model"
            />
          </div>
          <div class="form-group">
            <label v-text="t$('shipperfinderservice4App.tag.tripItem')" for="tag-tripItem"></label>
            <select
              class="form-control"
              id="tag-tripItems"
              data-cy="tripItem"
              multiple
              name="tripItem"
              v-if="tag.tripItems !== undefined"
              v-model="tag.tripItems"
            >
              <option
                :value="getSelected(tag.tripItems, tripItemOption, 'id')"
                v-for="tripItemOption in tripItems"
                :key="tripItemOption.id"
              >
                {{ tripItemOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="t$('shipperfinderservice4App.tag.cargoRequestItem')" for="tag-cargoRequestItem"></label>
            <select
              class="form-control"
              id="tag-cargoRequestItems"
              data-cy="cargoRequestItem"
              multiple
              name="cargoRequestItem"
              v-if="tag.cargoRequestItems !== undefined"
              v-model="tag.cargoRequestItems"
            >
              <option
                :value="getSelected(tag.cargoRequestItems, cargoRequestItemOption, 'id')"
                v-for="cargoRequestItemOption in cargoRequestItems"
                :key="cargoRequestItemOption.id"
              >
                {{ cargoRequestItemOption.id }}
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
<script lang="ts" src="./tag-update.component.ts"></script>
