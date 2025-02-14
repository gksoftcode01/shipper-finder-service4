<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.item.home.createOrEditLabel"
          data-cy="ItemCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.item.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="item.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="item.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.item.name')" for="item-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="item-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.item.nameEn')" for="item-nameEn"></label>
            <input
              type="text"
              class="form-control"
              name="nameEn"
              id="item-nameEn"
              data-cy="nameEn"
              :class="{ valid: !v$.nameEn.$invalid, invalid: v$.nameEn.$invalid }"
              v-model="v$.nameEn.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.item.nameAr')" for="item-nameAr"></label>
            <input
              type="text"
              class="form-control"
              name="nameAr"
              id="item-nameAr"
              data-cy="nameAr"
              :class="{ valid: !v$.nameAr.$invalid, invalid: v$.nameAr.$invalid }"
              v-model="v$.nameAr.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.item.nameFr')" for="item-nameFr"></label>
            <input
              type="text"
              class="form-control"
              name="nameFr"
              id="item-nameFr"
              data-cy="nameFr"
              :class="{ valid: !v$.nameFr.$invalid, invalid: v$.nameFr.$invalid }"
              v-model="v$.nameFr.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.item.nameDe')" for="item-nameDe"></label>
            <input
              type="text"
              class="form-control"
              name="nameDe"
              id="item-nameDe"
              data-cy="nameDe"
              :class="{ valid: !v$.nameDe.$invalid, invalid: v$.nameDe.$invalid }"
              v-model="v$.nameDe.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.item.nameUrdu')" for="item-nameUrdu"></label>
            <input
              type="text"
              class="form-control"
              name="nameUrdu"
              id="item-nameUrdu"
              data-cy="nameUrdu"
              :class="{ valid: !v$.nameUrdu.$invalid, invalid: v$.nameUrdu.$invalid }"
              v-model="v$.nameUrdu.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.item.isActive')" for="item-isActive"></label>
            <input
              type="checkbox"
              class="form-check"
              name="isActive"
              id="item-isActive"
              data-cy="isActive"
              :class="{ valid: !v$.isActive.$invalid, invalid: v$.isActive.$invalid }"
              v-model="v$.isActive.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.item.defaultUOM')" for="item-defaultUOM"></label>
            <input
              type="text"
              class="form-control"
              name="defaultUOM"
              id="item-defaultUOM"
              data-cy="defaultUOM"
              :class="{ valid: !v$.defaultUOM.$invalid, invalid: v$.defaultUOM.$invalid }"
              v-model="v$.defaultUOM.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.item.itemType')" for="item-itemType"></label>
            <select class="form-control" id="item-itemType" data-cy="itemType" name="itemType" v-model="item.itemType">
              <option :value="null"></option>
              <option
                :value="item.itemType && itemTypeOption.id === item.itemType.id ? item.itemType : itemTypeOption"
                v-for="itemTypeOption in itemTypes"
                :key="itemTypeOption.id"
              >
                {{ itemTypeOption.name }}
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
<script lang="ts" src="./item-update.component.ts"></script>
