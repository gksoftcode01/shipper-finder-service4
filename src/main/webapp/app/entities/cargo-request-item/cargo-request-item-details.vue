<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cargoRequestItem">
        <h2 class="jh-entity-heading" data-cy="cargoRequestItemDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.cargoRequestItem.detail.title')"></span> {{ cargoRequestItem.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.cargoRequestItem.maxQty')"></span>
          </dt>
          <dd>
            <span>{{ cargoRequestItem.maxQty }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.cargoRequestItem.photoUrl')"></span>
          </dt>
          <dd>
            <span>{{ cargoRequestItem.photoUrl }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.cargoRequestItem.item')"></span>
          </dt>
          <dd>
            <div v-if="cargoRequestItem.item">
              <router-link :to="{ name: 'ItemView', params: { itemId: cargoRequestItem.item.id } }">{{
                cargoRequestItem.item.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.cargoRequestItem.unit')"></span>
          </dt>
          <dd>
            <div v-if="cargoRequestItem.unit">
              <router-link :to="{ name: 'UnitView', params: { unitId: cargoRequestItem.unit.id } }">{{
                cargoRequestItem.unit.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.cargoRequestItem.tag')"></span>
          </dt>
          <dd>
            <span v-for="(tag, i) in cargoRequestItem.tags" :key="tag.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'TagView', params: { tagId: tag.id } }">{{ tag.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.cargoRequestItem.cargoRequest')"></span>
          </dt>
          <dd>
            <div v-if="cargoRequestItem.cargoRequest">
              <router-link :to="{ name: 'CargoRequestView', params: { cargoRequestId: cargoRequestItem.cargoRequest.id } }">{{
                cargoRequestItem.cargoRequest.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="cargoRequestItem.id"
          :to="{ name: 'CargoRequestItemEdit', params: { cargoRequestItemId: cargoRequestItem.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cargo-request-item-details.component.ts"></script>
