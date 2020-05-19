import Vue from 'vue';
import vuetify from './vuetify';
import store from './store';
import router from './router';
import App from './App.vue';
import 'material-design-icons-iconfont/dist/material-design-icons.css'
import axios from 'axios';
import DatetimePicker from 'vuetify-datetime-picker';
import utility from './utility/utility';

Vue.config.productionTip = false

Vue.use(DatetimePicker);
Vue.prototype.$utility = utility;

function createAxiosResponseInterceptor() {
  const interceptor = axios.interceptors.response.use(
    response => { return response; },
    error => {
      const{
        config,
        response: {status}
      } = error;
      
      //AKO JE ZAHTEV KA LOGIN-U I REGISTRACIJI, ONDA SVAKAKO NE MOZES DOBITI 401
      if(config.url.includes("login") || config.url.includes("register") || config.url.includes("refresh")){
        return Promise.reject(error);
      }

      //AKO ORIGINALNI ZAHTEV NE SADRZI JWT, ONDA NEMA SMISLA NI POKUSAVATI REFRESH TOKEN
      if(!Object.prototype.hasOwnProperty.call(config.headers, 'Authorization')){
        return Promise.reject(error);
      }

      if(status == 401){
        axios.interceptors.response.eject(interceptor); //prevent loop
        return store
          .dispatch("korisnici/refresh_token")
          .then(({data}) => {
            console.log("Uspesan refesh");
            store.commit("korisnici/setKorisnik", data);
            config.headers['Authorization'] = `Bearer ${data.accessToken}`;
            return axios(config);
          })
          .catch(() => {
            console.log("Neuspesan refesh");
            store.dispatch("korisnici/reset");
            store.dispatch("layout/reset");
            router.push({name: "login"});
            return Promise.reject(error);
          }).finally(createAxiosResponseInterceptor);
      }
      else{
        return error.response;
      }
    }
  );
}

new Vue({
  store,
  router,
  vuetify,
  render: h => h(App),
}).$mount('#app')

createAxiosResponseInterceptor();
