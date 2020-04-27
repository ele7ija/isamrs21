import Vue from 'vue';
import Router from 'vue-router';
import Login from '@/components/Login';
import Registracija from '@/components/Registracija';
import ONama from '@/components/ONama';
import UserDashboard from "@/components/dashboards/UserDashboard";
import TipPregleda from "@/components/tipovi_pregleda/TipPregleda";
import Sala from "@/components/sale/Sala";
import Osoblje from "@/components/osoblje/Osoblje";
import Pacijenti from "@/components/pacijenti/Pacijenti";
import Klinike from '@/components/pacijenti/klinike/Klinike';
import KlinikaPage from '@/components/pacijenti/klinike/KlinikaPage';
import store from '@/store/index';
//Plugins
Vue.use(Router)

let router = new Router({
  routes : [
    // Zajednicke rute
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
      path: '/o-nama',
      name: 'o-nama',
      component: ONama,
      meta: {
        authen: false
      }
    },
    {
      path: '/home',
      name: 'home',
      component: UserDashboard,
      meta: {
        authen: true,
        author: ''
      },
    },
    // Admin klinike
    {
      path: '/tipovi_pregleda',
      name: 'tipovi_pregleda',
      component: TipPregleda,
      meta: {
        authen: true,
        author: 'admin-klinike'
      },
    },
    {
      path: '/sale',
      name: 'sala',
      component: Sala,
      meta: {
        authen: true,
        author: 'admin-klinike'
      },
    },
    // Pacijent
    {
      path: '/osoblje',
      name: 'osoblje',
      component: Osoblje,
      meta: {
        authen: true,
        author: 'admin-klinike'
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
    },
    {
      path: '/pacijent/klinike',
      name: 'pacijent-klinike',
      component: Klinike,
      meta: {
        authen: true,
        author: 'pacijent'
      },
    },
    {
      path: '/pacijent/istorija',
      name: 'pacijent-istorija',
      component: Pacijenti,
      meta: {
        authen: true,
        author: 'pacijent'
      },
    },
    {
      path: '/pacijent/zdravstveni-karton',
      name: 'pacijent-zdravstveni-karton',
      component: Pacijenti,
      meta: {
        authen: true,
        author: 'pacijent'
      },
    },
    {
      path: '/pacijent/profil',
      name: 'pacijent-profil',
      component: Pacijenti,
      meta: {
        authen: true,
        author: 'pacijent'
      },
    },
    {
      path: '/pacijent/klinika/:klinikaId',
      name: 'pacijent-klinika',
      component: KlinikaPage,
      props: true,
      meta: {
        authen: true,
        author: 'pacijent'
      }
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
    // A certain authority
    if (korisnik.role !== to.meta.author && to.meta.author != '') {
      next(`/home`);
      return;
    }    
    else{
      next();
      return;
    }
  }
  else{
    if (korisnik !== null) {
      next(`/home`)
      return;
    }
    next();
  }
})

export default router;