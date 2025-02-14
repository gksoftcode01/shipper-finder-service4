<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.unit.home.createOrEditLabel"
          data-cy="UnitCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.unit.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="unit.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="unit.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.unit.name')" for="unit-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="unit-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.unit.nameEn')" for="unit-nameEn"></label>
            <input
              type="text"
              class="form-control"
              name="nameEn"
              id="unit-nameEn"
              data-cy="nameEn"
              :class="{ valid: !v$.nameEn.$invalid, invalid: v$.nameEn.$invalid }"
              v-model="v$.nameEn.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.unit.nameAr')" for="unit-nameAr"></label>
            <input
              type="text"
              class="form-control"
              name="nameAr"
              id="unit-nameAr"
              data-cy="nameAr"
              :class="{ valid: !v$.nameAr.$invalid, invalid: v$.nameAr.$invalid }"
              v-model="v$.nameAr.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.unit.nameFr')" for="unit-nameFr"></label>
            <input
              type="text"
              class="form-control"
              name="nameFr"
              id="unit-nameFr"
              data-cy="nameFr"
              :class="{ valid: !v$.nameFr.$invalid, invalid: v$.nameFr.$invalid }"
              v-model="v$.nameFr.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.unit.nameDe')" for="unit-nameDe"></label>
            <input
              type="text"
              class="form-control"
              name="nameDe"
              id="unit-nameDe"
              data-cy="nameDe"
              :class="{ valid: !v$.nameDe.$invalid, invalid: v$.nameDe.$invalid }"
              v-model="v$.nameDe.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.unit.nameUrdu')" for="unit-nameUrdu"></label>
            <input
              type="text"
              class="form-control"
              name="nameUrdu"
              id="unit-nameUrdu"
              data-cy="nameUrdu"
              :class="{ valid: !v$.nameUrdu.$invalid, invalid: v$.nameUrdu.$invalid }"
              v-model="v$.nameUrdu.$model"
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
<script lang="ts" src="./unit-update.component.ts"></script>
