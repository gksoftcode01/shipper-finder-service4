<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="shipperfinderservice4App.subscribeType.home.createOrEditLabel"
          data-cy="SubscribeTypeCreateUpdateHeading"
          v-text="t$('shipperfinderservice4App.subscribeType.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="subscribeType.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="subscribeType.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('shipperfinderservice4App.subscribeType.type')" for="subscribe-type-type"></label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !v$.type.$invalid, invalid: v$.type.$invalid }"
              v-model="v$.type.$model"
              id="subscribe-type-type"
              data-cy="type"
            >
              <option
                v-for="subscribeTypeEnum in subscribeTypeEnumValues"
                :key="subscribeTypeEnum"
                :value="subscribeTypeEnum"
                :label="t$('shipperfinderservice4App.SubscribeTypeEnum.' + subscribeTypeEnum)"
              >
                {{ subscribeTypeEnum }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.nameEn')"
              for="subscribe-type-nameEn"
            ></label>
            <input
              type="text"
              class="form-control"
              name="nameEn"
              id="subscribe-type-nameEn"
              data-cy="nameEn"
              :class="{ valid: !v$.nameEn.$invalid, invalid: v$.nameEn.$invalid }"
              v-model="v$.nameEn.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.nameAr')"
              for="subscribe-type-nameAr"
            ></label>
            <input
              type="text"
              class="form-control"
              name="nameAr"
              id="subscribe-type-nameAr"
              data-cy="nameAr"
              :class="{ valid: !v$.nameAr.$invalid, invalid: v$.nameAr.$invalid }"
              v-model="v$.nameAr.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.nameFr')"
              for="subscribe-type-nameFr"
            ></label>
            <input
              type="text"
              class="form-control"
              name="nameFr"
              id="subscribe-type-nameFr"
              data-cy="nameFr"
              :class="{ valid: !v$.nameFr.$invalid, invalid: v$.nameFr.$invalid }"
              v-model="v$.nameFr.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.nameDe')"
              for="subscribe-type-nameDe"
            ></label>
            <input
              type="text"
              class="form-control"
              name="nameDe"
              id="subscribe-type-nameDe"
              data-cy="nameDe"
              :class="{ valid: !v$.nameDe.$invalid, invalid: v$.nameDe.$invalid }"
              v-model="v$.nameDe.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.nameUrdu')"
              for="subscribe-type-nameUrdu"
            ></label>
            <input
              type="text"
              class="form-control"
              name="nameUrdu"
              id="subscribe-type-nameUrdu"
              data-cy="nameUrdu"
              :class="{ valid: !v$.nameUrdu.$invalid, invalid: v$.nameUrdu.$invalid }"
              v-model="v$.nameUrdu.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.details')"
              for="subscribe-type-details"
            ></label>
            <textarea
              class="form-control"
              name="details"
              id="subscribe-type-details"
              data-cy="details"
              :class="{ valid: !v$.details.$invalid, invalid: v$.details.$invalid }"
              v-model="v$.details.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.detailsEn')"
              for="subscribe-type-detailsEn"
            ></label>
            <input
              type="text"
              class="form-control"
              name="detailsEn"
              id="subscribe-type-detailsEn"
              data-cy="detailsEn"
              :class="{ valid: !v$.detailsEn.$invalid, invalid: v$.detailsEn.$invalid }"
              v-model="v$.detailsEn.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.detailsAr')"
              for="subscribe-type-detailsAr"
            ></label>
            <input
              type="text"
              class="form-control"
              name="detailsAr"
              id="subscribe-type-detailsAr"
              data-cy="detailsAr"
              :class="{ valid: !v$.detailsAr.$invalid, invalid: v$.detailsAr.$invalid }"
              v-model="v$.detailsAr.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.detailsFr')"
              for="subscribe-type-detailsFr"
            ></label>
            <input
              type="text"
              class="form-control"
              name="detailsFr"
              id="subscribe-type-detailsFr"
              data-cy="detailsFr"
              :class="{ valid: !v$.detailsFr.$invalid, invalid: v$.detailsFr.$invalid }"
              v-model="v$.detailsFr.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.detailsDe')"
              for="subscribe-type-detailsDe"
            ></label>
            <input
              type="text"
              class="form-control"
              name="detailsDe"
              id="subscribe-type-detailsDe"
              data-cy="detailsDe"
              :class="{ valid: !v$.detailsDe.$invalid, invalid: v$.detailsDe.$invalid }"
              v-model="v$.detailsDe.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('shipperfinderservice4App.subscribeType.detailsUrdu')"
              for="subscribe-type-detailsUrdu"
            ></label>
            <input
              type="text"
              class="form-control"
              name="detailsUrdu"
              id="subscribe-type-detailsUrdu"
              data-cy="detailsUrdu"
              :class="{ valid: !v$.detailsUrdu.$invalid, invalid: v$.detailsUrdu.$invalid }"
              v-model="v$.detailsUrdu.$model"
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
<script lang="ts" src="./subscribe-type-update.component.ts"></script>
