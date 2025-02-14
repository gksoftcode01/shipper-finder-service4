<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cargoMsg">
        <h2 class="jh-entity-heading" data-cy="cargoMsgDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.cargoMsg.detail.title')"></span> {{ cargoMsg.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.cargoMsg.msg')"></span>
          </dt>
          <dd>
            <span>{{ cargoMsg.msg }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.cargoMsg.fromUserEncId')"></span>
          </dt>
          <dd>
            <span>{{ cargoMsg.fromUserEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.cargoMsg.toUserEncId')"></span>
          </dt>
          <dd>
            <span>{{ cargoMsg.toUserEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.cargoMsg.cargoRequestId')"></span>
          </dt>
          <dd>
            <span>{{ cargoMsg.cargoRequestId }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="cargoMsg.id" :to="{ name: 'CargoMsgEdit', params: { cargoMsgId: cargoMsg.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cargo-msg-details.component.ts"></script>
