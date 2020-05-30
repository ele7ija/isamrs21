<template>
<div>
  <v-data-table
    :items="_posete"
    :headers="headers"
    show-expand
    single-expand
  >
    <template v-slot:top>
      <v-toolbar flat color="blue lighten-3">
        <v-toolbar-title>Obavljeni pregledi</v-toolbar-title>
      </v-toolbar>
    </template>
    <template v-slot:body.prepend>
      <tr>
        <td>
          <v-datetime-picker v-model="pocetak" :text-field-props="textFieldProps" date-format="dd.MM.yyyy" :key="key1"></v-datetime-picker>
        </td>
        <td>
          <v-datetime-picker v-model="kraj" :text-field-props="textFieldProps" date-format="dd.MM.yyyy" :key="key2"></v-datetime-picker>
        </td>
        <td>
          <v-select :items="_tipoviPregleda" v-model="tipPregleda"></v-select>
        </td>
        <td>
          <v-btn @click="ponisti()">Poništi</v-btn>
        </td>
      </tr>
    </template>
    <template v-slot:expanded-item="{ headers, item }">
      <td :colspan="headers.length">
        <div v-if="null != item.bolest">
          <v-text-field v-model="item.bolest.naziv" readonly label="Utvrđena bolest" class="mt-5"/>
        </div>
        <v-textarea
          outlined
          :value="item.opis"
          label="Opis simtpoma"
          readonly
        ></v-textarea>
      </td>
    </template>

    <!-- za izmenu pregleda -->
    <template v-slot:item.actions=" {item} ">
        <v-icon
          small
          class="mr-2 green--text text--darken-1"
          @click="editItem(item)">
        mdi-pencil
        </v-icon>
    </template>
  </v-data-table>
  <!-- dialog za izmenu pregleda-->
  <v-dialog v-model="dialogZaIzmenu" width="500" >
    <v-form v-model="isFormValid" ref="myForm">
      <v-card>
        <v-card-title>
          <span class="headline"> Izmeni opis </span>
        </v-card-title>
        
        <!-- opis pregleda -->
        <v-card-text>
        <v-textarea
        outlined
        label="Opis pregleda" 
        hint="Napisati opis pregleda i kakve probleme pacijent ima."
        :rules=opisRule
        v-model="newItem.opis"
        ></v-textarea>
        </v-card-text> 
 
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1"  text  @click="save()" :disabled="!isFormValid">
          Sačuvaj
        </v-btn>
        <v-btn color="blue darken-1" text @click="close()">
          Poništi
        </v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
</div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';
export default {
  name: 'TabelaPoseta',

  data: function(){
    return {
      key1: 0,
      key2: 2,
      pocetak: null,
      textFieldProps: {
        appendIcon: 'event'
      },
      kraj: null,
      tipPregleda: '',
      headers: [
        { text: 'Početak pregleda', value: 'pocetak', sortable: false,
          filter: (value) => {
            if(!this.pocetak)
              return true;
            let date = this.$utility.stringToDate(value);
            if(!this.kraj)
              return date.getTime() == this.pocetak.getTime();
            return date >= this.pocetak && date <= this.kraj;
          }
        },
        { text: 'Kraj pregleda', value: 'kraj', sortable: false,
          filter: (value) => {
            if(!this.kraj)
              return true;
            let date = this.$utility.stringToDate(value);
            if(!this.pocetak)
              return date.getTime() == this.kraj.getTime();
            return date >= this.pocetak && date <= this.kraj;
          }
        },
        { text: 'Tip pregleda', value: 'tipPregleda', sortable: false,
          filter: (value) => {
            if(!this.tipPregleda)
              return true;
            return value == this.tipPregleda;
          }
        },
        { text: 'Detalji', value: 'data-table-expand' },
        {
          text: 'Izmeni',
          value: 'actions',
          sortable: false,
        },
      ],

      //izmena posete
      dialogZaIzmenu: false,
      isFormValid: true,
      newItem: {
        opis: undefined,
        posetaId: undefined,
      },
      opisRule: [
        v => !!v || 'Opis trenutne posete je obavezan',
        v => (v && v.length <= 1000) || 'Opis ima najviše 1000 karaktera'
      ],
    };
  },
  computed: {
    ...mapGetters({
      posete: 'pacijenti/getPoseteOdabranogPacijenta',
      tipoviPregleda: 'tipoviPregleda/getTipoviPregleda'
    }),
    _posete(){
      if(this.posete){
        let obavljenePosete = this.posete.filter(x => {
          return !!x.opis 
        });
        return obavljenePosete.map(x => {
          let date1 = new Date(x.pregled.pocetakPregleda);
          let date2 = new Date(x.pregled.krajPregleda);
          date1 = this.$utility.handleTimeZone(date1);
          date2 = this.$utility.handleTimeZone(date2);
          return{
            id: x.id,
            pocetak: this.$utility.formatDate(date1),
            kraj: this.$utility.formatDate(date2),
            tipPregleda: x.pregled.tipPregleda.naziv,
            bolest: x.bolest,
            opis: x.opis
          };
        });
      }else
        return [];
    },
    _tipoviPregleda(){
      return this.tipoviPregleda.map(x => x.naziv);
    },
  },

  methods: {
    ...mapActions({
      updatePoseta: 'pacijenti/updatePoseta',
    }),
    ponisti(){
      this.pocetak = null;
      this.kraj = null;
      this.tipPregleda = '';
      this.key1 += 1;
      this.key2 += 1;
    },

    //izmena posete
    editItem(item){
      this.newItem.opis = item.opis;
      this.newItem.posetaId = item.id;
      this.dialogZaIzmenu = true;
    },
    save(){
      this.updatePoseta(this.newItem);
      this.close();
    },
    close(){
      this.dialogZaIzmenu = false;
    },
    resetForm(){
      this.$refs.myForm.resetValidation(); //internal vue method
      this.newItem = {
        posetaId: undefined,
        opis: undefined,
      };
    },
  }
}
</script>

<style>

</style>