<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tripMsg">
        <h2 class="jh-entity-heading" data-cy="tripMsgDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.tripMsg.detail.title')"></span> {{ tripMsg.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.tripMsg.msg')"></span>
          </dt>
          <dd>
            <span>{{ tripMsg.msg }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tripMsg.fromUserEncId')"></span>
          </dt>
          <dd>
            <span>{{ tripMsg.fromUserEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tripMsg.toUserEncId')"></span>
          </dt>
          <dd>
            <span>{{ tripMsg.toUserEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.tripMsg.tripId')"></span>
          </dt>
          <dd>
            <span>{{ tripMsg.tripId }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="tripMsg.id" :to="{ name: 'TripMsgEdit', params: { tripMsgId: tripMsg.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./trip-msg-details.component.ts"></script>
