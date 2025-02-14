<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.itemType.home.createOrEditLabel"
          data-cy="ItemTypeCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.itemType.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="itemType.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="itemType.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.itemType.name')" for="item-type-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="item-type-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.itemType.nameEn')" for="item-type-nameEn"></label>
            <input
              type="text"
              class="form-control"
              name="nameEn"
              id="item-type-nameEn"
              data-cy="nameEn"
              :class="{ valid: !v$.nameEn.$invalid, invalid: v$.nameEn.$invalid }"
              v-model="v$.nameEn.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.itemType.nameAr')" for="item-type-nameAr"></label>
            <input
              type="text"
              class="form-control"
              name="nameAr"
              id="item-type-nameAr"
              data-cy="nameAr"
              :class="{ valid: !v$.nameAr.$invalid, invalid: v$.nameAr.$invalid }"
              v-model="v$.nameAr.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.itemType.nameFr')" for="item-type-nameFr"></label>
            <input
              type="text"
              class="form-control"
              name="nameFr"
              id="item-type-nameFr"
              data-cy="nameFr"
              :class="{ valid: !v$.nameFr.$invalid, invalid: v$.nameFr.$invalid }"
              v-model="v$.nameFr.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.itemType.nameDe')" for="item-type-nameDe"></label>
            <input
              type="text"
              class="form-control"
              name="nameDe"
              id="item-type-nameDe"
              data-cy="nameDe"
              :class="{ valid: !v$.nameDe.$invalid, invalid: v$.nameDe.$invalid }"
              v-model="v$.nameDe.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.itemType.nameUrdu')" for="item-type-nameUrdu"></label>
            <input
              type="text"
              class="form-control"
              name="nameUrdu"
              id="item-type-nameUrdu"
              data-cy="nameUrdu"
              :class="{ valid: !v$.nameUrdu.$invalid, invalid: v$.nameUrdu.$invalid }"
              v-model="v$.nameUrdu.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.itemType.isActive')" for="item-type-isActive"></label>
            <input
              type="checkbox"
              class="form-check"
              name="isActive"
              id="item-type-isActive"
              data-cy="isActive"
              :class="{ valid: !v$.isActive.$invalid, invalid: v$.isActive.$invalid }"
              v-model="v$.isActive.$model"
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
<script lang="ts" src="./item-type-update.component.ts"></script>
