import Vue from 'vue';
import Router from 'vue-router';
import Login from '@/components/Login';
import Registracija from '@/components/Registracija';
import ONama from '@/components/ONama';
import UserDashboard from "@/components/dashboards/UserDashboard";
import TipPregleda from "@/components/tipovi_pregleda/TipPregleda";
import Sala from "@/components/sale/Sala";
import Osoblje from "@/components/osoblje/Osoblje";
import PreglediMain from "@/components/pregledi/PreglediMain"
import Pacijenti from "@/components/pacijenti/Pacijenti";
import KlinikaAdmin from '@/components/klinike/KlinikaAdmin';
import Klinike from '@/components/pacijenti/klinike/Klinike';
import KlinikaPage from '@/components/pacijenti/klinike/KlinikaPage';
import RezervacijaPregleda from '@/components/pacijenti/klinike/RezervacijaPregleda';
import Istorija from '@/components/pacijenti/klinike/Istorija';
import PacijentiHomePage from '@/components/pacijenti/PacijentiHomePage';

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
    {
      path: '/pacijent/home',
      name: 'pacijent-home',
      component: PacijentiHomePage,
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
    {
      path: '/klinika',
      name: 'klinika_admin',
      component: KlinikaAdmin,
      meta: {
        authen: true,
        author: 'admin-klinike'
      },
    },
    {
      path: '/pregledi',
      name: 'pregledi_admin',
      component: PreglediMain,
      meta: {
        authen: true,
        author: 'admin-klinike'
      }
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
      component: Istorija,
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
    },
    {
      path: '/pacijent/rezervacija-pregleda',
      name: 'rezervacija-pregleda',
      component: RezervacijaPregleda,
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
      // Moralo zbog rezervisanih pregleda
      if (korisnik.role === 'pacijent' && to.path==='/home') {
        next('/pacijent/home');
        return;
      }
      next();
      return;
    }
  }
  else{
    if (korisnik !== null) {
      // Moralo zbog rezervisanih pregleda
      if (korisnik.role === 'pacijent') {
        next('/pacijent/home');
        return;
      }
      next(`/home`)
      return;
    }
    next();
  }
})

export default router;