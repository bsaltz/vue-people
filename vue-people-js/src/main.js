// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import BootstrapVue from 'bootstrap-vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import {library} from '@fortawesome/fontawesome-svg-core'
import {
  faAngleDoubleLeft,
  faAngleDoubleRight,
  faAngleLeft,
  faAngleRight,
  faAngleUp,
  faAngleDown,
  faEdit,
  faTrash,
  faAddressBook,
  faTag,
  faMobile,
  faPhone,
  faFax,
  faEnvelope
} from '@fortawesome/free-solid-svg-icons'
import {
  faStackOverflow,
  faFacebook,
  faTwitter,
  faBitbucket,
  faGithub
} from '@fortawesome/free-brands-svg-icons'
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'
import store from './store'

library.add(
  faAngleDoubleLeft,
  faAngleDoubleRight,
  faAngleLeft,
  faAngleRight,
  faAngleUp,
  faAngleDown,
  faEdit,
  faTrash,
  faAddressBook,
  faTag,
  faMobile,
  faPhone,
  faFax,
  faEnvelope,
  faStackOverflow,
  faFacebook,
  faTwitter,
  faBitbucket,
  faGithub
)

Vue.config.productionTip = false
Vue.use(BootstrapVue)
Vue.use(VueAxios, axios)
Vue.component('font-awesome-icon', FontAwesomeIcon)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {App},
  template: '<App/>'
})
