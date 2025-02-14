<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="country">
        <h2 class="jh-entity-heading" data-cy="countryDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.country.detail.title')"></span> {{ country.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.name')"></span>
          </dt>
          <dd>
            <span>{{ country.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.localName')"></span>
          </dt>
          <dd>
            <span>{{ country.localName }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.iso2')"></span>
          </dt>
          <dd>
            <span>{{ country.iso2 }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.iso3')"></span>
          </dt>
          <dd>
            <span>{{ country.iso3 }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.numericCode')"></span>
          </dt>
          <dd>
            <span>{{ country.numericCode }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.phoneCode')"></span>
          </dt>
          <dd>
            <span>{{ country.phoneCode }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.currency')"></span>
          </dt>
          <dd>
            <span>{{ country.currency }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.currencyName')"></span>
          </dt>
          <dd>
            <span>{{ country.currencyName }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.currencySymbol')"></span>
          </dt>
          <dd>
            <span>{{ country.currencySymbol }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.emoji')"></span>
          </dt>
          <dd>
            <span>{{ country.emoji }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.country.emojiU')"></span>
          </dt>
          <dd>
            <span>{{ country.emojiU }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="country.id" :to="{ name: 'CountryEdit', params: { countryId: country.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./country-details.component.ts"></script>
