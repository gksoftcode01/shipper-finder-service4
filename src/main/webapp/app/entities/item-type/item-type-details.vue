<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="itemType">
        <h2 class="jh-entity-heading" data-cy="itemTypeDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.itemType.detail.title')"></span> {{ itemType.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.itemType.name')"></span>
          </dt>
          <dd>
            <span>{{ itemType.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.itemType.nameEn')"></span>
          </dt>
          <dd>
            <span>{{ itemType.nameEn }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.itemType.nameAr')"></span>
          </dt>
          <dd>
            <span>{{ itemType.nameAr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.itemType.nameFr')"></span>
          </dt>
          <dd>
            <span>{{ itemType.nameFr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.itemType.nameDe')"></span>
          </dt>
          <dd>
            <span>{{ itemType.nameDe }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.itemType.nameUrdu')"></span>
          </dt>
          <dd>
            <span>{{ itemType.nameUrdu }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.itemType.isActive')"></span>
          </dt>
          <dd>
            <span>{{ itemType.isActive }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="itemType.id" :to="{ name: 'ItemTypeEdit', params: { itemTypeId: itemType.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./item-type-details.component.ts"></script>
