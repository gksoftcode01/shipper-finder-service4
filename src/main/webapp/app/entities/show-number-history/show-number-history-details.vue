<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="showNumberHistory">
        <h2 class="jh-entity-heading" data-cy="showNumberHistoryDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.showNumberHistory.detail.title')"></span> {{ showNumberHistory.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.showNumberHistory.createdDate')"></span>
          </dt>
          <dd>
            <span v-if="showNumberHistory.createdDate">{{ formatDateLong(showNumberHistory.createdDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.showNumberHistory.actionByEncId')"></span>
          </dt>
          <dd>
            <span>{{ showNumberHistory.actionByEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.showNumberHistory.entityType')"></span>
          </dt>
          <dd>
            <span>{{ showNumberHistory.entityType }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.showNumberHistory.entityEncId')"></span>
          </dt>
          <dd>
            <span>{{ showNumberHistory.entityEncId }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="showNumberHistory.id"
          :to="{ name: 'ShowNumberHistoryEdit', params: { showNumberHistoryId: showNumberHistory.id } }"
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

<script lang="ts" src="./show-number-history-details.component.ts"></script>
