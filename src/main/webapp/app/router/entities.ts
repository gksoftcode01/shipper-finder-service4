import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const StateProvince = () => import('@/entities/state-province/state-province.vue');
const StateProvinceUpdate = () => import('@/entities/state-province/state-province-update.vue');
const StateProvinceDetails = () => import('@/entities/state-province/state-province-details.vue');

const Country = () => import('@/entities/country/country.vue');
const CountryUpdate = () => import('@/entities/country/country-update.vue');
const CountryDetails = () => import('@/entities/country/country-details.vue');

const ReportAbuse = () => import('@/entities/report-abuse/report-abuse.vue');
const ReportAbuseUpdate = () => import('@/entities/report-abuse/report-abuse-update.vue');
const ReportAbuseDetails = () => import('@/entities/report-abuse/report-abuse-details.vue');

const UserRate = () => import('@/entities/user-rate/user-rate.vue');
const UserRateUpdate = () => import('@/entities/user-rate/user-rate-update.vue');
const UserRateDetails = () => import('@/entities/user-rate/user-rate-details.vue');

const ItemType = () => import('@/entities/item-type/item-type.vue');
const ItemTypeUpdate = () => import('@/entities/item-type/item-type-update.vue');
const ItemTypeDetails = () => import('@/entities/item-type/item-type-details.vue');

const Item = () => import('@/entities/item/item.vue');
const ItemUpdate = () => import('@/entities/item/item-update.vue');
const ItemDetails = () => import('@/entities/item/item-details.vue');

const AppUser = () => import('@/entities/app-user/app-user.vue');
const AppUserUpdate = () => import('@/entities/app-user/app-user-update.vue');
const AppUserDetails = () => import('@/entities/app-user/app-user-details.vue');

const AppUserDevice = () => import('@/entities/app-user-device/app-user-device.vue');
const AppUserDeviceUpdate = () => import('@/entities/app-user-device/app-user-device-update.vue');
const AppUserDeviceDetails = () => import('@/entities/app-user-device/app-user-device-details.vue');

const SubscribeType = () => import('@/entities/subscribe-type/subscribe-type.vue');
const SubscribeTypeUpdate = () => import('@/entities/subscribe-type/subscribe-type-update.vue');
const SubscribeTypeDetails = () => import('@/entities/subscribe-type/subscribe-type-details.vue');

const SubscribeTypeDetail = () => import('@/entities/subscribe-type-detail/subscribe-type-detail.vue');
const SubscribeTypeDetailUpdate = () => import('@/entities/subscribe-type-detail/subscribe-type-detail-update.vue');
const SubscribeTypeDetailDetails = () => import('@/entities/subscribe-type-detail/subscribe-type-detail-details.vue');

const UserSubscribe = () => import('@/entities/user-subscribe/user-subscribe.vue');
const UserSubscribeUpdate = () => import('@/entities/user-subscribe/user-subscribe-update.vue');
const UserSubscribeDetails = () => import('@/entities/user-subscribe/user-subscribe-details.vue');

const Languages = () => import('@/entities/languages/languages.vue');
const LanguagesUpdate = () => import('@/entities/languages/languages-update.vue');
const LanguagesDetails = () => import('@/entities/languages/languages-details.vue');

const Trip = () => import('@/entities/trip/trip.vue');
const TripUpdate = () => import('@/entities/trip/trip-update.vue');
const TripDetails = () => import('@/entities/trip/trip-details.vue');

const Tag = () => import('@/entities/tag/tag.vue');
const TagUpdate = () => import('@/entities/tag/tag-update.vue');
const TagDetails = () => import('@/entities/tag/tag-details.vue');

const Unit = () => import('@/entities/unit/unit.vue');
const UnitUpdate = () => import('@/entities/unit/unit-update.vue');
const UnitDetails = () => import('@/entities/unit/unit-details.vue');

const TripItem = () => import('@/entities/trip-item/trip-item.vue');
const TripItemUpdate = () => import('@/entities/trip-item/trip-item-update.vue');
const TripItemDetails = () => import('@/entities/trip-item/trip-item-details.vue');

const CargoRequest = () => import('@/entities/cargo-request/cargo-request.vue');
const CargoRequestUpdate = () => import('@/entities/cargo-request/cargo-request-update.vue');
const CargoRequestDetails = () => import('@/entities/cargo-request/cargo-request-details.vue');

const CargoRequestItem = () => import('@/entities/cargo-request-item/cargo-request-item.vue');
const CargoRequestItemUpdate = () => import('@/entities/cargo-request-item/cargo-request-item-update.vue');
const CargoRequestItemDetails = () => import('@/entities/cargo-request-item/cargo-request-item-details.vue');

const ShowNumberHistory = () => import('@/entities/show-number-history/show-number-history.vue');
const ShowNumberHistoryUpdate = () => import('@/entities/show-number-history/show-number-history-update.vue');
const ShowNumberHistoryDetails = () => import('@/entities/show-number-history/show-number-history-details.vue');

const TripMsg = () => import('@/entities/trip-msg/trip-msg.vue');
const TripMsgUpdate = () => import('@/entities/trip-msg/trip-msg-update.vue');
const TripMsgDetails = () => import('@/entities/trip-msg/trip-msg-details.vue');

const CargoMsg = () => import('@/entities/cargo-msg/cargo-msg.vue');
const CargoMsgUpdate = () => import('@/entities/cargo-msg/cargo-msg-update.vue');
const CargoMsgDetails = () => import('@/entities/cargo-msg/cargo-msg-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'state-province',
      name: 'StateProvince',
      component: StateProvince,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'state-province/new',
      name: 'StateProvinceCreate',
      component: StateProvinceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'state-province/:stateProvinceId/edit',
      name: 'StateProvinceEdit',
      component: StateProvinceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'state-province/:stateProvinceId/view',
      name: 'StateProvinceView',
      component: StateProvinceDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country',
      name: 'Country',
      component: Country,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country/new',
      name: 'CountryCreate',
      component: CountryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country/:countryId/edit',
      name: 'CountryEdit',
      component: CountryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country/:countryId/view',
      name: 'CountryView',
      component: CountryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'report-abuse',
      name: 'ReportAbuse',
      component: ReportAbuse,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'report-abuse/new',
      name: 'ReportAbuseCreate',
      component: ReportAbuseUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'report-abuse/:reportAbuseId/edit',
      name: 'ReportAbuseEdit',
      component: ReportAbuseUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'report-abuse/:reportAbuseId/view',
      name: 'ReportAbuseView',
      component: ReportAbuseDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-rate',
      name: 'UserRate',
      component: UserRate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-rate/new',
      name: 'UserRateCreate',
      component: UserRateUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-rate/:userRateId/edit',
      name: 'UserRateEdit',
      component: UserRateUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-rate/:userRateId/view',
      name: 'UserRateView',
      component: UserRateDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-type',
      name: 'ItemType',
      component: ItemType,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-type/new',
      name: 'ItemTypeCreate',
      component: ItemTypeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-type/:itemTypeId/edit',
      name: 'ItemTypeEdit',
      component: ItemTypeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-type/:itemTypeId/view',
      name: 'ItemTypeView',
      component: ItemTypeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item',
      name: 'Item',
      component: Item,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item/new',
      name: 'ItemCreate',
      component: ItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item/:itemId/edit',
      name: 'ItemEdit',
      component: ItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item/:itemId/view',
      name: 'ItemView',
      component: ItemDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user',
      name: 'AppUser',
      component: AppUser,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user/new',
      name: 'AppUserCreate',
      component: AppUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user/:appUserId/edit',
      name: 'AppUserEdit',
      component: AppUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user/:appUserId/view',
      name: 'AppUserView',
      component: AppUserDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user-device',
      name: 'AppUserDevice',
      component: AppUserDevice,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user-device/new',
      name: 'AppUserDeviceCreate',
      component: AppUserDeviceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user-device/:appUserDeviceId/edit',
      name: 'AppUserDeviceEdit',
      component: AppUserDeviceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user-device/:appUserDeviceId/view',
      name: 'AppUserDeviceView',
      component: AppUserDeviceDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscribe-type',
      name: 'SubscribeType',
      component: SubscribeType,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscribe-type/new',
      name: 'SubscribeTypeCreate',
      component: SubscribeTypeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscribe-type/:subscribeTypeId/edit',
      name: 'SubscribeTypeEdit',
      component: SubscribeTypeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscribe-type/:subscribeTypeId/view',
      name: 'SubscribeTypeView',
      component: SubscribeTypeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscribe-type-detail',
      name: 'SubscribeTypeDetail',
      component: SubscribeTypeDetail,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscribe-type-detail/new',
      name: 'SubscribeTypeDetailCreate',
      component: SubscribeTypeDetailUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscribe-type-detail/:subscribeTypeDetailId/edit',
      name: 'SubscribeTypeDetailEdit',
      component: SubscribeTypeDetailUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscribe-type-detail/:subscribeTypeDetailId/view',
      name: 'SubscribeTypeDetailView',
      component: SubscribeTypeDetailDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-subscribe',
      name: 'UserSubscribe',
      component: UserSubscribe,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-subscribe/new',
      name: 'UserSubscribeCreate',
      component: UserSubscribeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-subscribe/:userSubscribeId/edit',
      name: 'UserSubscribeEdit',
      component: UserSubscribeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-subscribe/:userSubscribeId/view',
      name: 'UserSubscribeView',
      component: UserSubscribeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'languages',
      name: 'Languages',
      component: Languages,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'languages/new',
      name: 'LanguagesCreate',
      component: LanguagesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'languages/:languagesId/edit',
      name: 'LanguagesEdit',
      component: LanguagesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'languages/:languagesId/view',
      name: 'LanguagesView',
      component: LanguagesDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip',
      name: 'Trip',
      component: Trip,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip/new',
      name: 'TripCreate',
      component: TripUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip/:tripId/edit',
      name: 'TripEdit',
      component: TripUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip/:tripId/view',
      name: 'TripView',
      component: TripDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag',
      name: 'Tag',
      component: Tag,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag/new',
      name: 'TagCreate',
      component: TagUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag/:tagId/edit',
      name: 'TagEdit',
      component: TagUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tag/:tagId/view',
      name: 'TagView',
      component: TagDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'unit',
      name: 'Unit',
      component: Unit,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'unit/new',
      name: 'UnitCreate',
      component: UnitUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'unit/:unitId/edit',
      name: 'UnitEdit',
      component: UnitUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'unit/:unitId/view',
      name: 'UnitView',
      component: UnitDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip-item',
      name: 'TripItem',
      component: TripItem,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip-item/new',
      name: 'TripItemCreate',
      component: TripItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip-item/:tripItemId/edit',
      name: 'TripItemEdit',
      component: TripItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip-item/:tripItemId/view',
      name: 'TripItemView',
      component: TripItemDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-request',
      name: 'CargoRequest',
      component: CargoRequest,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-request/new',
      name: 'CargoRequestCreate',
      component: CargoRequestUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-request/:cargoRequestId/edit',
      name: 'CargoRequestEdit',
      component: CargoRequestUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-request/:cargoRequestId/view',
      name: 'CargoRequestView',
      component: CargoRequestDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-request-item',
      name: 'CargoRequestItem',
      component: CargoRequestItem,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-request-item/new',
      name: 'CargoRequestItemCreate',
      component: CargoRequestItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-request-item/:cargoRequestItemId/edit',
      name: 'CargoRequestItemEdit',
      component: CargoRequestItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-request-item/:cargoRequestItemId/view',
      name: 'CargoRequestItemView',
      component: CargoRequestItemDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'show-number-history',
      name: 'ShowNumberHistory',
      component: ShowNumberHistory,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'show-number-history/new',
      name: 'ShowNumberHistoryCreate',
      component: ShowNumberHistoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'show-number-history/:showNumberHistoryId/edit',
      name: 'ShowNumberHistoryEdit',
      component: ShowNumberHistoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'show-number-history/:showNumberHistoryId/view',
      name: 'ShowNumberHistoryView',
      component: ShowNumberHistoryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip-msg',
      name: 'TripMsg',
      component: TripMsg,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip-msg/new',
      name: 'TripMsgCreate',
      component: TripMsgUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip-msg/:tripMsgId/edit',
      name: 'TripMsgEdit',
      component: TripMsgUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'trip-msg/:tripMsgId/view',
      name: 'TripMsgView',
      component: TripMsgDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-msg',
      name: 'CargoMsg',
      component: CargoMsg,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-msg/new',
      name: 'CargoMsgCreate',
      component: CargoMsgUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-msg/:cargoMsgId/edit',
      name: 'CargoMsgEdit',
      component: CargoMsgUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cargo-msg/:cargoMsgId/view',
      name: 'CargoMsgView',
      component: CargoMsgDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
