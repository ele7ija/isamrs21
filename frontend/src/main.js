import Vue from 'vue';
import vuetify from './vuetify';
import store from './store';
import router from './router';
import App from './App.vue';

Vue.config.productionTip = false


new Vue({
  store,
  router,
  vuetify,
  render: h => h(App),
}).$mount('#app')
