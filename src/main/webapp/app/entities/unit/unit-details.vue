<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="unit">
        <h2 class="jh-entity-heading" data-cy="unitDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.unit.detail.title')"></span> {{ unit.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.unit.name')"></span>
          </dt>
          <dd>
            <span>{{ unit.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.unit.nameEn')"></span>
          </dt>
          <dd>
            <span>{{ unit.nameEn }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.unit.nameAr')"></span>
          </dt>
          <dd>
            <span>{{ unit.nameAr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.unit.nameFr')"></span>
          </dt>
          <dd>
            <span>{{ unit.nameFr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.unit.nameDe')"></span>
          </dt>
          <dd>
            <span>{{ unit.nameDe }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.unit.nameUrdu')"></span>
          </dt>
          <dd>
            <span>{{ unit.nameUrdu }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="unit.id" :to="{ name: 'UnitEdit', params: { unitId: unit.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./unit-details.component.ts"></script>
