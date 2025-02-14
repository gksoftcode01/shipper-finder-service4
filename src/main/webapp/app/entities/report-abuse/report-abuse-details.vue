<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="reportAbuse">
        <h2 class="jh-entity-heading" data-cy="reportAbuseDetailsHeading">
          <span v-text="t$('shipperfinderservice4App.reportAbuse.detail.title')"></span> {{ reportAbuse.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperfinderservice4App.reportAbuse.reportByEncId')"></span>
          </dt>
          <dd>
            <span>{{ reportAbuse.reportByEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.reportAbuse.reportedAgainstEncId')"></span>
          </dt>
          <dd>
            <span>{{ reportAbuse.reportedAgainstEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.reportAbuse.reportDate')"></span>
          </dt>
          <dd>
            <span v-if="reportAbuse.reportDate">{{ formatDateLong(reportAbuse.reportDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.reportAbuse.reportData')"></span>
          </dt>
          <dd>
            <span>{{ reportAbuse.reportData }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperfinderservice4App.reportAbuse.reportStatus')"></span>
          </dt>
          <dd>
            <span v-text="t$('shipperfinderservice4App.ReportStatus.' + reportAbuse.reportStatus)"></span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="reportAbuse.id"
          :to="{ name: 'ReportAbuseEdit', params: { reportAbuseId: reportAbuse.id } }"
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

<script lang="ts" src="./report-abuse-details.component.ts"></script>
