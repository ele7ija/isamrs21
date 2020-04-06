import Vuex from 'vuex';
import Vue from 'vue';
import tipoviPregleda from './modules/tipoviPregleda';
//Plugins
Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    tipoviPregleda
  }
})