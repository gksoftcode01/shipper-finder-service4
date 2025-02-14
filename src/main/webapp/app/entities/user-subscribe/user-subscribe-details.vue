<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="userSubscribe">
        <h2 class="jh-entity-heading" data-cy="userSubscribeDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.userSubscribe.detail.title')"></span> {{ userSubscribe.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.userSubscribe.fromDate')"></span>
          </dt>
          <dd>
            <span v-if="userSubscribe.fromDate">{{ formatDateLong(userSubscribe.fromDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.userSubscribe.toDate')"></span>
          </dt>
          <dd>
            <span v-if="userSubscribe.toDate">{{ formatDateLong(userSubscribe.toDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.userSubscribe.isActive')"></span>
          </dt>
          <dd>
            <span>{{ userSubscribe.isActive }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.userSubscribe.subscribedUserEncId')"></span>
          </dt>
          <dd>
            <span>{{ userSubscribe.subscribedUserEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.userSubscribe.subscribeType')"></span>
          </dt>
          <dd>
            <div v-if="userSubscribe.subscribeType">
              <router-link :to="{ name: 'SubscribeTypeView', params: { subscribeTypeId: userSubscribe.subscribeType.id } }">{{
                userSubscribe.subscribeType.type
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="userSubscribe.id"
          :to="{ name: 'UserSubscribeEdit', params: { userSubscribeId: userSubscribe.id } }"
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

<script lang="ts" src="./user-subscribe-details.component.ts"></script>
