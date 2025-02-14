<template>
  <div>
    <b-dropdown-item to="/state-province">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.stateProvince')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/country">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.country')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/report-abuse">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.reportAbuse')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user-rate">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.userRate')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/item-type">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.itemType')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/item">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.item')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/app-user">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.appUser')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/app-user-device">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.appUserDevice')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/subscribe-type">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.subscribeType')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/subscribe-type-detail">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.subscribeTypeDetail')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user-subscribe">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.userSubscribe')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/languages">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.languages')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/trip">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.trip')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/tag">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.tag')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/unit">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.unit')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/trip-item">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.tripItem')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/cargo-request">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.cargoRequest')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/cargo-request-item">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.cargoRequestItem')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/show-number-history">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.showNumberHistory')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/trip-msg">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.tripMsg')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/cargo-msg">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.cargoMsg')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
