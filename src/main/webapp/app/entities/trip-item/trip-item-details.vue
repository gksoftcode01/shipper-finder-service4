<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tripItem">
        <h2 class="jh-entity-heading" data-cy="tripItemDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.tripItem.detail.title')"></span> {{ tripItem.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.tripItem.itemPrice')"></span>
          </dt>
          <dd>
            <span>{{ tripItem.itemPrice }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tripItem.maxQty')"></span>
          </dt>
          <dd>
            <span>{{ tripItem.maxQty }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tripItem.item')"></span>
          </dt>
          <dd>
            <div v-if="tripItem.item">
              <router-link :to="{ name: 'ItemView', params: { itemId: tripItem.item.id } }">{{ tripItem.item.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tripItem.unit')"></span>
          </dt>
          <dd>
            <div v-if="tripItem.unit">
              <router-link :to="{ name: 'UnitView', params: { unitId: tripItem.unit.id } }">{{ tripItem.unit.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tripItem.tag')"></span>
          </dt>
          <dd>
            <span v-for="(tag, i) in tripItem.tags" :key="tag.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'TagView', params: { tagId: tag.id } }">{{ tag.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tripItem.trip')"></span>
          </dt>
          <dd>
            <div v-if="tripItem.trip">
              <router-link :to="{ name: 'TripView', params: { tripId: tripItem.trip.id } }">{{ tripItem.trip.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="tripItem.id" :to="{ name: 'TripItemEdit', params: { tripItemId: tripItem.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./trip-item-details.component.ts"></script>
