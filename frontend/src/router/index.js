import Vue from 'vue';
import Router from 'vue-router';
import Login from '@/components/Login';
import Registracija from '@/components/Registracija';
import ONama from '@/components/ONama';
import UserDashboard from "@/components/dashboards/UserDashboard";
import Profil from "@/components/profil/Profil"
import TipPregleda from "@/components/tipovi_pregleda/TipPregleda";
import SalaMain from "@/components/sale/SalaMain";
import Osoblje from "@/components/osoblje/Osoblje";
import PreglediMain from "@/components/pregledi/PreglediMain";
import UpitiPreglediAdmin from "@/components/upiti/UpitiPreglediAdmin";
import ZahteviZaGodisnji from "@/components/zahtevi_godisnji/ZahteviZaGodisnji";
import KlinikaAdmin from '@/components/klinike/KlinikaAdmin';
import Klinike from '@/components/pacijenti/klinike/Klinike';
import KlinikaPage from '@/components/pacijenti/klinike/KlinikaPage';
import RezervacijaPregleda from '@/components/pacijenti/klinike/RezervacijaPregleda';
import Istorija from '@/components/pacijenti/istorija/Istorija';
import PacijentProfil from '@/components/pacijenti/profil/PacijentProfil';
import Karton from '@/components/pacijenti/karton/Karton';
import Lekari from '@/components/pacijenti/klinike/Lekari';
//admin klinickog centra
import AdminiKlinickogCentra from '@/components/admin_klinickog_centra/AdminiKlinickogCentra'
import AdminiKlinike from '@/components/admin_klinickog_centra/AdminiKlinike'
import KlinikeFromAdminCentra from '@/components/admin_klinickog_centra/Klinike'
import ZahteviZaRegistraciju from '@/components/admin_klinickog_centra/ZahteviZaRegistraciju'
import Sifarnik from '@/components/admin_klinickog_centra/Sifarnik'

//lekar
import PacijentiSearch from '@/components/lekar/PacijentiSearch';
import RadniKalendarLekar from '@/components/lekar/RadniKalendarLekar';
import OdsustvoLekar from '@/components/lekar/OdsustvoLekar';
import ProfilPacijenta from "@/components/lekar/pacijent/ProfilPacijenta";
import PacijentiHomePage from '@/components/pacijenti/PacijentiHomePage';

//medicinska sestra
import ListaPacijenata from '@/components/med_sestra/ListaPacijenata'

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
      path: '/profil',
      name: 'profil',
      component: Profil,
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
      component: SalaMain,
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
    {
      path: '/upiti_pregledi',
      name: 'upiti_pregledi_admin',
      component: UpitiPreglediAdmin,
      meta: {
        authen: true,
        author: 'admin-klinike'
      }
    },
    {
      path: '/zahtevi_godisnji',
      name: 'zahtevi_godisnji',
      component: ZahteviZaGodisnji,
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
    // {
    //   path: '/pacijenti',
    //   name: 'pacijenti',
    //   component: Pacijenti,
    //   meta: {
    //     authen: true,
    //     author: 'pacijent'
    //   },
    // },
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
      component: Karton,
      meta: {
        authen: true,
        author: 'pacijent'
      },
    },
    {
      path: '/pacijent/profil',
      name: 'pacijent-profil',
      component: PacijentProfil,
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
      path: '/pacijent/lekari/:klinikaId',
      name: 'pacijent-lekari',
      component: Lekari,
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
    },

    //Admin klinickog centra
    {
      path: '/admini_klinickog_centra',
      name: 'admini_klinickog_centra',
      component: AdminiKlinickogCentra,
      meta: {
        authen: true,
        author: 'admin-klinickog-centra'
      },
    },
    {
      path: '/admini_klinika',
      name: 'admini_klinika',
      component: AdminiKlinike,
      meta: {
        authen: true,
        author: 'admin-klinickog-centra'
      },
    },
    {
      path: '/klinike',
      name: 'klinike_from_admin_centra',
      component: KlinikeFromAdminCentra,
      meta: {
        authen: true,
        author: 'admin-klinickog-centra'
      },
    },
    {
      path: '/zahtevi_za_registraciju',
      name: 'zahtevi_za_registraciju',
      component: ZahteviZaRegistraciju,
      meta: {
        authen: true,
        author: 'admin-klinickog-centra'
      }
    },
    {
      path: '/sifarnik',
      name: 'sifarnik',
      component: Sifarnik,
      meta: {
        authen: true,
        author: 'admin-klinickog-centra'
      }
    },
    
    //lekar
    {
      path: '/pacijetni_klinike',
      name: 'PacijentiSearch',
      component: PacijentiSearch,
      meta: {
        authen: true,
        author: 'lekar'
      },
    },
    {
      path: '/radni_kalendar',
      name: 'RadniKalendarLekar',
      component: RadniKalendarLekar,
      meta: {
        authen: true,
        author: 'lekar'
      },
    },
    {
      path: '/odsustvo',
      name: 'OdsustvoLekar',
      component: OdsustvoLekar,
      meta: {
        authen: true,
        author: 'lekar'
      }
    },
    {
      path: '/profil_pacijenta',
      name: 'ProfilPacijenta',
      component: ProfilPacijenta,
      meta: {
        authen: true,
        author: 'lekar'
      }
    },

    //medicinska sestra
    {
      path: '/pacijetni_klinike',
      name: 'lista_pacijenata',
      component: ListaPacijenata,
      meta: {
        authen: true,
        author: 'medicinska-sestra'
      }
    },

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
    /*let profil = store.getters['profil/getProfil'];
    if(profil.poslednjaPromenaSifre == null){
      next({name: 'profil'}); //obavezna promena sifre pri prvom logovanju korisnika
    }*/
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