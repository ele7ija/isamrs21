import Vue from 'vue';
import Router from 'vue-router';
import Login from '@/components/Login';
import Registracija from '@/components/Registracija';
import UserDashboard from "@/components/dashboards/UserDashboard";
import TipPregleda from "@/components/tipovi_pregleda/TipPregleda";
import Sala from "@/components/sale/Sala";
import Pacijenti from "@/components/pacijenti/Pacijenti";
import store from '@/store/index';
//Plugins
Vue.use(Router)

let router = new Router({
  routes : [
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: {
        authen: false
      }
    },
    {
      path: '',
      redirect: '/login',
      meta: {
        authen: false
      }
    },
    {
      path: '/registracija',
      name: 'registracija',
      component: Registracija,
      meta: {
        authen: false
      }
    },
    {
      path: '/home/:userType',
      name: 'home',
      component: UserDashboard,
      props: true,
      meta: {
        authen: true,
        author: ''
      },
    },
    {
      path: '/tipovi_pregleda',
      name: 'tipovi_pregleda',
      component: TipPregleda,
      meta: {
        authen: true,
        author: 'admin_klinike'
      },
    },
    {
      path: '/sale',
      name: 'sala',
      component: Sala,
      meta: {
        authen: true,
        author: 'admin_klinike'
      },
    },
    {
      path: '/pacijenti',
      name: 'pacijenti',
      component: Pacijenti,
      meta: {
        authen: true,
        author: 'pacijent'
      },
    }
  ]
});

router.beforeEach((to, from, next) => {
  let korisnik = store.getters['korisnici/getKorisnik'];
  // undefined ukoliko je neko od nas zab da stavi guard za rutu
  // ili ruta ne postoji
  if (to.meta.authen || to.meta.authen == undefined) {
    // AUTHENTICATION CHECK
    if (korisnik === null) {
      next({name: 'login'});
      return;
    }
    // AUTHORIZATION CHECK
    // An authority given by prop
    if (to.meta.author === ''){
      if (korisnik.role !== to.params.userType) {
        next(`/home/${korisnik.role}`);
        return;
      }
      next();
    }
    // A certain authority
    else if (korisnik.role !== to.meta.author) {
      next(`/home/${korisnik.role}`);
      return;
    }    
    else{
      next();
      return;
    }
  }
  else{
    if (korisnik !== null) {
      next(`/home/${korisnik.role}`)
      return;
    }
    next();
  }
})

export default router;