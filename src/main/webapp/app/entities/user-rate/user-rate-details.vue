<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="userRate">
        <h2 class="jh-entity-heading" data-cy="userRateDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.userRate.detail.title')"></span> {{ userRate.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.userRate.rate')"></span>
          </dt>
          <dd>
            <span>{{ userRate.rate }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.userRate.note')"></span>
          </dt>
          <dd>
            <span>{{ userRate.note }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.userRate.rateDate')"></span>
          </dt>
          <dd>
            <span v-if="userRate.rateDate">{{ formatDateLong(userRate.rateDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.userRate.ratedByEncId')"></span>
          </dt>
          <dd>
            <span>{{ userRate.ratedByEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.userRate.ratedEncId')"></span>
          </dt>
          <dd>
            <span>{{ userRate.ratedEncId }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="userRate.id" :to="{ name: 'UserRateEdit', params: { userRateId: userRate.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./user-rate-details.component.ts"></script>
