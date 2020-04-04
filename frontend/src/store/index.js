import Vuex from 'vuex';
import Vue from 'vue';
import template_module from './modules/template_module';
//Plugins
Vue.use(Vuex);

export default {
  modules: {
    template_module
  }
}