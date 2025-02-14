import { defineComponent, provide } from 'vue';

import StateProvinceService from './state-province/state-province.service';
import CountryService from './country/country.service';
import ReportAbuseService from './report-abuse/report-abuse.service';
import UserRateService from './user-rate/user-rate.service';
import ItemTypeService from './item-type/item-type.service';
import ItemService from './item/item.service';
import AppUserService from './app-user/app-user.service';
import AppUserDeviceService from './app-user-device/app-user-device.service';
import SubscribeTypeService from './subscribe-type/subscribe-type.service';
import SubscribeTypeDetailService from './subscribe-type-detail/subscribe-type-detail.service';
import UserSubscribeService from './user-subscribe/user-subscribe.service';
import LanguagesService from './languages/languages.service';
import TripService from './trip/trip.service';
import TagService from './tag/tag.service';
import UnitService from './unit/unit.service';
import TripItemService from './trip-item/trip-item.service';
import CargoRequestService from './cargo-request/cargo-request.service';
import CargoRequestItemService from './cargo-request-item/cargo-request-item.service';
import ShowNumberHistoryService from './show-number-history/show-number-history.service';
import TripMsgService from './trip-msg/trip-msg.service';
import CargoMsgService from './cargo-msg/cargo-msg.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('stateProvinceService', () => new StateProvinceService());
    provide('countryService', () => new CountryService());
    provide('reportAbuseService', () => new ReportAbuseService());
    provide('userRateService', () => new UserRateService());
    provide('itemTypeService', () => new ItemTypeService());
    provide('itemService', () => new ItemService());
    provide('appUserService', () => new AppUserService());
    provide('appUserDeviceService', () => new AppUserDeviceService());
    provide('subscribeTypeService', () => new SubscribeTypeService());
    provide('subscribeTypeDetailService', () => new SubscribeTypeDetailService());
    provide('userSubscribeService', () => new UserSubscribeService());
    provide('languagesService', () => new LanguagesService());
    provide('tripService', () => new TripService());
    provide('tagService', () => new TagService());
    provide('unitService', () => new UnitService());
    provide('tripItemService', () => new TripItemService());
    provide('cargoRequestService', () => new CargoRequestService());
    provide('cargoRequestItemService', () => new CargoRequestItemService());
    provide('showNumberHistoryService', () => new ShowNumberHistoryService());
    provide('tripMsgService', () => new TripMsgService());
    provide('cargoMsgService', () => new CargoMsgService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
