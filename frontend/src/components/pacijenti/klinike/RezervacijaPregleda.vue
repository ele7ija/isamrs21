<template>
  <v-container fluid >
    <v-row justify="center">
      <v-col cols='7'>
        <v-card>
          <v-card-title>
            Rezervacija pregleda
          </v-card-title>
          <v-card-subtitle>
            Molimo proverite i dopunite podatke vezane za pregled
          </v-card-subtitle>
          <v-card-text>
            <v-container fluid class='pb-0'>
              <v-row>
                <v-col cols=5>
                  <v-date-picker
                    v-model='generisanDatum'
                    disabled
                    full-width
                    locale="sr">
                  </v-date-picker>
                  <v-list>
                  <v-list-item>
                    <v-list-item-content>
                      <v-col cols=6>
                        <v-text-field
                        v-model='casoviPocetka'
                        disabled
                        outlined
                        label='Početak'>
                      </v-text-field>
                      </v-col>
                      <v-col cols=6>
                        <v-text-field
                        v-model='casoviKraja'
                        disabled
                        outlined
                        label='Kraj'>
                      </v-text-field>
                      </v-col>
                      
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                </v-col>
                <v-col cols=7>
                <v-list>
                  <v-list-item>
                    <v-list-item-content>
                      <v-text-field
                        outlined
                        v-model='odabraniPregled.tipPregleda.naziv'
                        label='Naziv pregleda'
                        disabled="">
                      </v-text-field>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-content>
                      <v-text-field
                        outlined
                        v-model='odabraniPregled.tipPregleda.opis'
                        label='Opis pregleda'
                        disabled="">
                      </v-text-field>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-content>
                      <v-text-field
                        outlined
                        v-model='generisanLekar'
                        label='Lekar'
                        disabled="">
                      </v-text-field>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item>
                    <v-card
                      outlined>
                      <v-card-subtitle class='pb-0'>
                        Adresa
                      </v-card-subtitle>
                      <v-container>
                        <v-row>
                          <v-col cols=5 class='pa-0 pl-4 pr-4'>
                            <v-img
                              src='https://vojvodinainfo.rs/wp-content/uploads/2018/12/urgentni-centar-klinicki-centar-kcv-vojvodina-Mapa-dobrog-provoda-Srbija-Top-10.jpg'>
                            </v-img>
                          </v-col>
                          <v-col class='pt-0 pb-0'>
                            <v-row>
                              <v-col class='pa-0 subtitle-2'> 
                                Klinika "{{odabranaKlinika.naziv}}"
                              </v-col>  
                            </v-row>
                            <v-row>
                              <v-col class='pa-0 caption'> 
                                {{odabranaKlinika.adresa}}
                              </v-col>
                            </v-row>
                          </v-col>
                        </v-row>
                      </v-container>
                    </v-card>
                    
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-content>
                      <v-text-field
                        outlined
                        background-color="green lighten-4"
                        v-model='napomena'
                        label='Napomena'
                        hint='Upisati Vašu napomenu'
                        persistent-hint="">

                      </v-text-field>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
          <v-card-actions class="justify-center pb-8">
            <v-btn 
              color='success'
              outlined
              @click='potvrdi'>
              <v-icon>mdi-check</v-icon>
              Potvrdi
            </v-btn>
            <v-btn 
              color='red'
              outlined
              @click='odustani'>
              <v-icon>mdi-cancel</v-icon>
              Odustani
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapState, mapMutations, mapActions } from 'vuex'
export default {
  name: 'RezervacijaPregleda',
  data: function() {
    return {
      napomena: ''
    }
  },
  computed: {
    ...mapState('klinike', [
      'odabraniPregled',
      'odabranaKlinika'
    ]),
    ...mapState('korisnici', [
      '_korisnik'
    ]),
    generisanDatum: function() {
      let datum = new Date(this.odabraniPregled.pocetakPregleda);
      return datum.toISOString().substr(0, 10);
    },
    generisanLekar: function() {
      return this.odabraniPregled.lekar.ime + ' ' + this.odabraniPregled.lekar.prezime;
    },
    casoviPocetka: function() {
      let date = new Date(this.odabraniPregled.pocetakPregleda);
      return ('0' + date.getHours()).slice(-2) + ':' +
        ('0' + date.getMinutes()).slice(-2);
    },
    casoviKraja: function() {
      let date = new Date(this.odabraniPregled.krajPregleda);
      return ('0' + date.getHours()).slice(-2) + ':' +
        ('0' + date.getMinutes()).slice(-2);
    }
  },
  methods: {
    ...mapMutations('klinike', 
      ['setOdabraniPregled']),
    ...mapActions('klinike',
      ['kreirajPosetu',
      'kreirajUpit']),
    odustani: function() {
      this.setOdabraniPregled(null);
      this.$router.go(-1)
    },
    potvrdi: function() {
      // let obj = {
      //   pregledId: this.odabraniPregled.id,
      //   opis: this.napomena
      // }
      // console.log(obj);
      // this.kreirajPosetu(obj)
      let obj = {
        odobren: false,
        potvrdjen: false,
        pocetakPregleda: this.odabraniPregled.pocetakPregleda,
        krajPregleda: this.odabraniPregled.krajPregleda,
        tipPregleda: this.odabraniPregled.tipPregleda.id,
        lekar: this.odabraniPregled.lekar.id,
        klinika: this.odabraniPregled.klinika.id,
        pacijent: this._korisnik.username
      }
      console.log(obj);
      this.kreirajUpit(obj)
    }
  },
  created() {
    console.log(this.odabraniPregled)
    if (this.odabraniPregled == null) {
      alert('Ne postoji odabran pregled')
      this.$router.push('/');
    }
  },

}
</script>

<style scoped>
  
</style>