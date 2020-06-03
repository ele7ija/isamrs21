const state = {
  optionDict: {
    'admin-klinike': [
      {
        title: 'Dashboard',
        action: 'mdi-view-dashboard',
        componentName: "home",
        items: []
      },
      { 
        title: 'Profil',
        action: 'mdi-account',
        componentName: 'profil',
        items: []
      },
      {
        title: 'Klinika',
        action: 'mdi-hospital-building',
        items: [
          { 
            title: 'Profil klinike',
            action: 'mdi-account-cog',
            componentName: 'klinika_admin'
          },
          { 
            title: 'Tipovi pregleda',
            action: 'mdi-alpha-t',
            componentName: 'tipovi_pregleda'
          },
          {
            title: 'Sale',
            action: 'mdi-alpha-s',
            componentName: 'sala'
          },
          {
            title: 'Osoblje',
            action: 'mdi-alpha-o',
            componentName: 'osoblje'
          },
          {
            title: 'Pregledi',
            action: 'mdi-alpha-p',
            componentName: 'pregledi_admin'
          },
          {
            title: 'Upiti za preglede',
            action: 'mdi-alpha-u',
            componentName: 'upiti_pregledi_admin'
          },
          {
            title: 'Upiti za odsustvo',
            action: 'mdi-beach',
            componentName: 'zahtevi_godisnji'
          },
          {
            title: 'Izveštaji poslovanja',
            action: 'mdi-file-chart',
            componentName: 'izvestaji'
          },
        ]
      }
      
    ],
    'pacijent': [
      {
        title: 'Dashboard',
        action: 'mdi-view-dashboard',
        componentName: "home",
        items: []
      },
      {
        title: 'Pacijent',
        action: 'mdi-account',
        items: [
          { 
            title: 'Klinike',
            action: 'mdi-hospital-building',
            componentName: 'pacijent-klinike'
          },
          { 
            title: 'Istorija',
            action: 'mdi-history',
            componentName: 'pacijent-istorija'
          },
          { 
            title: 'Zdravstveni karton',
            action: 'mdi-bottle-tonic-plus',
            componentName: 'pacijent-zdravstveni-karton'
          },
          { 
            title: 'Profil',
            action: 'mdi-account-cog',
            componentName: 'pacijent-profil'
          },
        ]
      }
    ],

    'admin-klinickog-centra' : [
      {
        title: 'Dashboard',
        action: 'mdi-view-dashboard',
        componentName: "home",
        items: []
      },
      { 
        title: 'Profil',
        action: 'mdi-account',
        componentName: 'profil',
        items: []
      },
      {
        title: 'Klinički centar',
        
        items: [
          { 
            title: 'Zahtevi za registraciju',
            action: 'mdi-receipt',
            componentName: 'zahtevi_za_registraciju',
            items:[]
          },
          { 
            title: 'Administratori klinika',
            action: 'mdi-account-supervisor',
            componentName: 'admini_klinika',
            items:[]
          },
          {
            title: 'Klinike',
            action: 'mdi-hospital-building',
            componentName: 'klinike_from_admin_centra',
            items:[]
          },
          {
            title: 'Šifranik dijagnoza i lekova',
            action: 'mdi-medical-bag',
            componentName: 'sifarnik',
            items:[]
          },
          { 
            title: 'Administratori kliničkog centra',
            action: 'mdi-account-supervisor',
            componentName: 'admini_klinickog_centra',
            items:[]
          },
        ]
      }
    ],
    'lekar': [
      {
        title: 'Dashboard',
        action: 'mdi-view-dashboard',
        componentName: "home",
        items: []
      },
      { 
        title: 'Profil',
        action: 'mdi-account',
        componentName: 'profil',
        items: []
      },
      {
        title: 'Klinika',
        action: 'mdi-hospital-building',
        items: [
          { 
            title: 'Pregled pacijenata',
            action: 'mdi-account-search',
            componentName: 'PacijentiSearch'
          },
          { 
            title: 'Radni kalendar',
            action: 'mdi-calendar',
            componentName: 'RadniKalendarLekar'
          },
          { 
            title: 'Odsustvo',
            action: 'mdi-beach',
            componentName: 'OdsustvoLekar'
          },
        ]
      } 
    ],

    'medicinska-sestra' : [
      {
        title: 'Dashboard',
        action: 'mdi-view-dashboard',
        componentName: "home",
        items: []
      },
      { 
        title: 'Profil',
        action: 'mdi-account',
        componentName: 'profil',
        items: []
      },
      {
        title: 'Klinika',
        action: 'mdi-hospital-building',
        items: [
          { 
            title: 'Lista pacijenata',
            action: 'mdi-account-search',
            componentName: 'lista_pacijenata'
          },
          { 
            title: 'Radni kalendar',
            action: 'mdi-calendar',
            componentName: 'kalendar_sestra'
          },
          { 
            title: 'Odsustvo',
            action: 'mdi-beach',
            componentName: 'odsustvo_sestra'
          },
        ]
      } 
    ],
  }
}
const getters = {
  getOptions: (state) => (userType) => {
    return state.optionDict[userType];
  }
}
const actions = {}
const mutations = {}

export default{
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}