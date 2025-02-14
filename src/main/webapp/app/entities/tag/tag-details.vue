<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tag">
        <h2 class="jh-entity-heading" data-cy="tagDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.tag.detail.title')"></span> {{ tag.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.tag.name')"></span>
          </dt>
          <dd>
            <span>{{ tag.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tag.nameEn')"></span>
          </dt>
          <dd>
            <span>{{ tag.nameEn }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tag.nameAr')"></span>
          </dt>
          <dd>
            <span>{{ tag.nameAr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tag.nameFr')"></span>
          </dt>
          <dd>
            <span>{{ tag.nameFr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tag.nameDe')"></span>
          </dt>
          <dd>
            <span>{{ tag.nameDe }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tag.nameUrdu')"></span>
          </dt>
          <dd>
            <span>{{ tag.nameUrdu }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tag.iconUrl')"></span>
          </dt>
          <dd>
            <span>{{ tag.iconUrl }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tag.tripItem')"></span>
          </dt>
          <dd>
            <span v-for="(tripItem, i) in tag.tripItems" :key="tripItem.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'TripItemView', params: { tripItemId: tripItem.id } }">{{ tripItem.id }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tag.cargoRequestItem')"></span>
          </dt>
          <dd>
            <span v-for="(cargoRequestItem, i) in tag.cargoRequestItems" :key="cargoRequestItem.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'CargoRequestItemView', params: { cargoRequestItemId: cargoRequestItem.id } }">{{
                cargoRequestItem.id
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="tag.id" :to="{ name: 'TagEdit', params: { tagId: tag.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tag-details.component.ts"></script>
