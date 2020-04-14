import Vue from 'vue';
import Router from 'vue-router';
import Login from '@/components/Login';
import Registracija from '@/components/Registracija';
import UserDashboard from "@/components/dashboards/UserDashboard";
import TipPregleda from "@/components/tipovi_pregleda/TipPregleda";
import Sala from "@/components/sale/Sala";
import Pacijenti from "@/components/pacijenti/Pacijenti";
//Plugins
Vue.use(Router)

export default new Router({
  routes : [
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '',
      redirect: '/login'
    },
    {
      path: '/registracija',
      name: 'registracija',
      component: Registracija  
    },
    {
      path: '/home/:userType',
      name: 'home',
      component: UserDashboard,
      props: true
    },
    {
      path: '/tipovi_pregleda',
      name: 'tipovi_pregleda',
      component: TipPregleda
    },
    {
      path: '/sale',
      name: 'sala',
      component: Sala
    },
    {
      path: '/pacijenti',
      name: 'pacijenti',
      component: Pacijenti
    }
  ]
});