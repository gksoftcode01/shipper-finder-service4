<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="stateProvince">
        <h2 class="jh-entity-heading" data-cy="stateProvinceDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.stateProvince.detail.title')"></span> {{ stateProvince.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.stateProvince.name')"></span>
          </dt>
          <dd>
            <span>{{ stateProvince.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.stateProvince.localName')"></span>
          </dt>
          <dd>
            <span>{{ stateProvince.localName }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.stateProvince.isoCode')"></span>
          </dt>
          <dd>
            <span>{{ stateProvince.isoCode }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.stateProvince.country')"></span>
          </dt>
          <dd>
            <div v-if="stateProvince.country">
              <router-link :to="{ name: 'CountryView', params: { countryId: stateProvince.country.id } }">{{
                stateProvince.country.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="stateProvince.id"
          :to="{ name: 'StateProvinceEdit', params: { stateProvinceId: stateProvince.id } }"
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

<script lang="ts" src="./state-province-details.component.ts"></script>
