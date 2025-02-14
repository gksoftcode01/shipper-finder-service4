<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="trip">
        <h2 class="jh-entity-heading" data-cy="tripDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.trip.detail.title')"></span> {{ trip.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.tripDate')"></span>
          </dt>
          <dd>
            <span v-if="trip.tripDate">{{ formatDateLong(trip.tripDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.arriveDate')"></span>
          </dt>
          <dd>
            <span v-if="trip.arriveDate">{{ formatDateLong(trip.arriveDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.maxWeight')"></span>
          </dt>
          <dd>
            <span>{{ trip.maxWeight }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.notes')"></span>
          </dt>
          <dd>
            <span>{{ trip.notes }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.createDate')"></span>
          </dt>
          <dd>
            <span v-if="trip.createDate">{{ formatDateLong(trip.createDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.isNegotiate')"></span>
          </dt>
          <dd>
            <span>{{ trip.isNegotiate }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.status')"></span>
          </dt>
          <dd>
            <span v-text="t$('shipperfinderservice4App.TripStatus.' + trip.status)"></span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.createdByEncId')"></span>
          </dt>
          <dd>
            <span>{{ trip.createdByEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.encId')"></span>
          </dt>
          <dd>
            <span>{{ trip.encId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.fromCountry')"></span>
          </dt>
          <dd>
            <div v-if="trip.fromCountry">
              <router-link :to="{ name: 'CountryView', params: { countryId: trip.fromCountry.id } }">{{
                trip.fromCountry.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.toCountry')"></span>
          </dt>
          <dd>
            <div v-if="trip.toCountry">
              <router-link :to="{ name: 'CountryView', params: { countryId: trip.toCountry.id } }">{{ trip.toCountry.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.fromState')"></span>
          </dt>
          <dd>
            <div v-if="trip.fromState">
              <router-link :to="{ name: 'StateProvinceView', params: { stateProvinceId: trip.fromState.id } }">{{
                trip.fromState.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.trip.toState')"></span>
          </dt>
          <dd>
            <div v-if="trip.toState">
              <router-link :to="{ name: 'StateProvinceView', params: { stateProvinceId: trip.toState.id } }">{{
                trip.toState.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="trip.id" :to="{ name: 'TripEdit', params: { tripId: trip.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./trip-details.component.ts"></script>
