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
            title: 'Pacijenti',
            action: 'mdi-account-circle-outline',
            componentName: 'pacijenti'
          },
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
            title: 'Administratori kliničkog centra',
            action: 'mdi-account-supervisor',
            componentName: 'admini_klinickog_centra',
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
            title: 'Zahtevi za registraciju',
            action: 'mdi-receipt',
            componentName: 'zahtevi_za_registraciju',
            items:[]
          },
          {
            title: 'dijagnoze i lekovi',
            action: 'mdi-medical-bag',
            componentName: 'sale',
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
    ]
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