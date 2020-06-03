<template>
  <v-row>
    <v-col cols="12" md="5">
      <v-menu
        ref="menu1"
        v-model="menu1"
        :close-on-content-click="false"
        :return-value.sync="pocetak"
        transition="scale-transition"
        offset-y
        min-width="290px"
      >
        <template v-slot:activator="{ on }">
          <v-text-field
            v-model="pocetakFormatted"
            label="Prvi dan"
            prepend-icon="mdi-calendar-range"
            readonly
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker v-if="!krajDate" v-model="pocetak" no-title scrollable>
          <v-spacer></v-spacer>
          <v-btn text color="red lighten-1" @click="menu1 = false">Odustani</v-btn>
          <v-btn text color="green lighten-1" @click="$refs.menu1.save(pocetak)">Potvrdi</v-btn>
        </v-date-picker>
         <v-date-picker v-else v-model="pocetak" no-title scrollable :max="krajDate.toISOString().substr(0, 10)">
          <v-spacer></v-spacer>
          <v-btn text color="red lighten-1" @click="menu1 = false">Odustani</v-btn>
          <v-btn text color="green lighten-1" @click="$refs.menu1.save(pocetak)">Potvrdi</v-btn>
        </v-date-picker>
      </v-menu>
    </v-col>
    <v-col cols="12" md="5" v-if="pocetakDate">
      <v-menu
        ref="menu2"
        v-model="menu2"
        :close-on-content-click="false"
        :return-value.sync="kraj"
        transition="scale-transition"
        offset-y
        min-width="290px"
      >
        <template v-slot:activator="{ on }">
          <v-text-field
            v-model="krajFormatted"
            label="Poslednji dan"
            prepend-icon="mdi-calendar-range"
            readonly
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker v-model="kraj" no-title scrollable :min="pocetakDate.toISOString().substr(0, 10)">
          <v-spacer></v-spacer>
          <v-btn text color="red lighten-1" @click="menu2 = false">Odustani</v-btn>
          <v-btn text color="green lighten-1" @click="$refs.menu2.save(kraj)">Potvrdi</v-btn>
        </v-date-picker>
      </v-menu>
    </v-col>
    <v-col cols="12" md="2">
      <v-btn @click="reset">Reset</v-btn>
    </v-col>
    <v-col cols="12" md="12" v-if="pocetakDate && krajDate">
      <CustomChart :chart-data="chartData" :options="chartOptions"></CustomChart>
    </v-col>
  </v-row>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
import CustomChart from './charts/CustomChart';
export default{
  name: "StatistikaPregleda",
  components: {
    CustomChart
  },
  data: function(){
    return{      
      menu1: false,
      menu2: false,

      pocetak: null, //string
      pocetakDate: null, //date
      pocetakFormatted: null, //formated string
      
      kraj: null, //string
      krajDate: null, //date
      krajFormatted: null, //formatted string
    };
  },
  watch: {
    pocetak (newValue) {
      if(newValue == null){
        this.pocetakDate = null;
        this.pocetakFormatted = null;
      }else{
        this.pocetakDate = new Date(newValue);
        this.pocetakFormatted = this.formatDate(this.pocetakDate);
      }
    },
    kraj (newValue) {
      if(newValue == null){
        this.krajDate = null;
        this.krajFormatted = null;
      }else{
        this.krajDate = new Date(newValue);
        this.krajFormatted = this.formatDate(this.krajDate);
      }
    },
  },
  computed:{
    ...mapGetters({
      nazivKlinike: "izvestaji/getNazivKlinike",
      pregledi: "izvestaji/getPreglediKlinike"
    }),
    chartData(){
      return {
        labels: this.chartLabels,
        datasets: this.chartDataSets
      };
    },
    chartLabels(){
      let retval = [];
      if(this.pocetakDate && this.krajDate){
        let startDate = this.$utility.stringToDate2(this.pocetakFormatted);
        while(startDate <= this.krajDate){
          retval.push(startDate.toLocaleDateString('sr'));
          startDate.setDate(startDate.getDate() + 1);
        }
      }
      return retval;
    },
    chartDataSets(){
      let retval = [];
      if(this.pocetakDate && this.krajDate){
        retval.push({
          'label': `Broj pregleda klinike ${this.nazivKlinike}`,
          'fill': false,
          'borderColor': "#7CB342",
          'data': this.chartLabels.map(x => {
            return this.pregledi.filter(y => x == new Date(y.pocetakPregleda).toLocaleDateString('sr')).length;
          })
        });
      }
      return retval;
    },
    chartOptions(){
      return {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          yAxes: [{
            display: true,
            scaleLabel: {
              display: true,
              labelString: 'Broj pregleda',
              fontSize: 15
            }
          }],
          xAxes: [{
            display: true,
            scaleLabel: {
              display: true,
              labelString: 'Datum',
              fontSize: 15
            }
          }]
        },
        legend: {
          display: true
        },
        tooltips: {
          enabled: true,
          mode: 'single',
          callbacks: {
            label: function(tooltipItems) {
              return tooltipItems.yLabel;
            }
          }
        }
      };
    }
  },
  created(){
    this.fetchRezervisaniPregledi();
  },
  methods: {
    ...mapActions({
      fetchRezervisaniPregledi: 'izvestaji/fetchRezervisaniPregledi'
    }),
    formatDate(date){
      return date.toLocaleDateString('sr');
    },
    reset(){
      this.pocetak = null;
      this.kraj = null;
    }
  }
}
</script>