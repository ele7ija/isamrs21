<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col lg='3' md='3' sm='6'>
        <PretragaLekari></PretragaLekari>
      </v-col>
      <v-col lg='7' md='7' sm='6'>
        <v-container fluid>
          <v-row 
            v-if='pretrazeniLekari.length!=0'>
            <v-col
              :cols=6
              v-for='lekar in pretrazeniLekari'
              :key='lekar.id'>
              <LekarCard 
                :lekar='lekar'>
              </LekarCard>
            </v-col>
          </v-row>
          <v-row
            v-if='nepretrazeniLekari.length!=0'>
            <v-col 
              :cols=6 
              v-for='lekar in nepretrazeniLekari'
              :key='lekar.id'>
              <LekarCard 
                :lekar='lekar'>
              </LekarCard>
            </v-col>
          </v-row>
        </v-container>
      </v-col>
    </v-row>
  </v-container>
  <!-- <div>
    <div v-for='lekar in lekari' :key='lekar.id'>
      {{lekar.ime}}
    </div>
  </div> -->
</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex';
import PretragaLekari from './PretragaLekari';
import LekarCard from './LekarCard';
export default {
  name: 'Lekari',
  props: ['klinikaId'],
  components: {
    PretragaLekari,
    LekarCard
  },
  data: function() {
    return {

    }
  },
  computed: {
    ...mapState('klinike', [
      'minOcenaLekara',
      'maxOcenaLekara'
    ]),
    ...mapState('osobljePacijent', [
      'lekari'
    ]),
    ...mapGetters('osobljePacijent', [
      'getProsecnaOcenaLekara'
    ]),
    ...mapGetters('klinike', [
      'getPretrazeniPregledi'
    ]),
    pregledi: function() {
      return this.getPretrazeniPregledi(this.klinikaId)
    },
    pretrazeniLekari: function() {
      let retval = new Set();
      if (this.lekari.length==0) {
          return [];
        }
      for (let pregled of this.pregledi){
        retval.add(pregled.lekar.id);
      }
      let r = [];
      retval.forEach((id) => {
        r.push(this.lekari.filter((x) => x.id === id)[0]);
      });
      let r2 = [];
      for (let lekar of r) {
        let po = this.getProsecnaOcenaLekara(lekar.id);
        if (po < this.minOcenaLekara || po > this.maxOcenaLekara) {
          continue;
        }
        r2.push(lekar);
      }
      return r2;
    },
    nepretrazeniLekari: function() {
      return this.lekari.filter((x) => {
        for (let lekar of this.pretrazeniLekari) {
          if (lekar.id === x.id) {
            return false;
          }
        }
        return true;
      })
    }
  },
  methods: {
    ...mapActions('osobljePacijent', [
      'pronadjiLekare',
      'loadAllOcene'
    ]),
    ...mapActions('klinike', [
      'dobaviPodatkeKlinikaPage',
    ]),
    getPreglediLekara: function(lekarId) {
      let retval = [];
      for (let pregled of this.pregledi) {
        if (pregled.lekar.id == lekarId) {
          retval.push(pregled);
        }
      }
      return retval;
    }
  },
  async created() {
    this.dobaviPodatkeKlinikaPage(this.klinikaId);
    await this.pronadjiLekare(this.klinikaId);
    await this.loadAllOcene();
  }
}
</script>

<style>

</style>