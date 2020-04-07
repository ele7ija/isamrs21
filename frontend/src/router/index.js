import Vue from 'vue';
import Router from 'vue-router';
import home from '@/components/Home';
import TipPregleda from "@/components/tipovi_pregleda/TipPregleda"
import Pacijenti from "@/components/pacijenti/Pacijenti"
//Plugins
Vue.use(Router)

export default new Router({
  routes : [
    {
      path: '/home',
      name: 'home',
      component: home
    },
    {
      path: '',
      redirect: '/home'
    },
    {
      path: '/tipovi_pregleda',
      name: 'tipovi_pregleda',
      component: TipPregleda
    },
    {
      path: '/pacijenti',
      name: 'pacijenti',
      component: Pacijenti
    }
  ]
});