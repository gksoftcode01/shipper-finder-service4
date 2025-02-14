<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="subscribeType">
        <h2 class="jh-entity-heading" data-cy="subscribeTypeDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.subscribeType.detail.title')"></span> {{ subscribeType.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.type')"></span>
          </dt>
          <dd>
            <span v-text="t$('shipperfinderservice4App.SubscribeTypeEnum.' + subscribeType.type)"></span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.nameEn')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.nameEn }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.nameAr')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.nameAr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.nameFr')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.nameFr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.nameDe')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.nameDe }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.nameUrdu')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.nameUrdu }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.details')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.details }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.detailsEn')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.detailsEn }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.detailsAr')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.detailsAr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.detailsFr')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.detailsFr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.detailsDe')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.detailsDe }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.subscribeType.detailsUrdu')"></span>
          </dt>
          <dd>
            <span>{{ subscribeType.detailsUrdu }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="subscribeType.id"
          :to="{ name: 'SubscribeTypeEdit', params: { subscribeTypeId: subscribeType.id } }"
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

<script lang="ts" src="./subscribe-type-details.component.ts"></script>
