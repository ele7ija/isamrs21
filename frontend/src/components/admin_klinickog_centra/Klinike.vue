<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="getAll"
      :search="search"
      class="elevation-1"
      :single-expand="true"
      item-key="id"
      show-expand
      >
      <template v-slot:top>
        <v-toolbar flat color="white">
          <v-toolbar-title>Klinike</v-toolbar-title>
          <v-divider
            class="mx-4"
            inset
            vertical
          ></v-divider>
          <v-spacer></v-spacer>
          
<!-- search bar  -->
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
          <v-divider
            class="mx-4"
            inset
            vertical
          ></v-divider>
          <v-spacer></v-spacer>


<!-- forma za unos ili izmenu unutar dialoga-->
          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on }">
              <v-btn color="primary"  class="mb-2" v-on="on">Dodaj</v-btn>
            </template>
            <v-form v-model="isFormValid">
              <v-card>
                <!-- naslov forme -->
                <v-card-title>
                  <span class="headline">{{formTitle}}</span>
                </v-card-title>
                <hr>

                <!-- elementi -->
                <v-card-text>
                  <v-container>                      
                    <v-text-field 
                    label="naziv" 
                    v-model="newItem.naziv" 
                    :rules="nazivRule"
                    required
                    ></v-text-field>
                    
                    <v-text-field
                    label="adresa"
                    v-model="newItem.adresa"
                    :rules="adresaRule"
                    required
                    ></v-text-field>

                    <v-text-field
                    label="grad"
                    v-model="newItem.grad"
                    :rules="gradRule"
                    required
                    ></v-text-field>


                    <v-text-field
                    label="drzava"
                    v-model="newItem.drzava"
                    :rules="drzavaRule"
                    required
                    ></v-text-field>


                    <v-text-field
                    label="opis"
                    v-model="newItem.opis"
                    >
                    <v-switch></v-switch>
                    
                    </v-text-field>

                  </v-container>
                </v-card-text>
                
                
                <!-- akcije -->
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="close">Nazad</v-btn>
                  <v-btn color="blue darken-1" text @click="save" :disabled="!isFormValid">Sačuvaj</v-btn>
                </v-card-actions>
              </v-card>
            </v-form>
          </v-dialog>
        </v-toolbar>
      </template>

<!-- opis klinike ide na expand -->
      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length" style="padding:15px " class="grey--text ">
        {{item.opis}} 
        </td>
      </template>


    </v-data-table>
  </div>
</template>

<script>
import {mapGetters, mapActions} from 'vuex';

export default {
  name: "Klinike",
  data: function(){
    return {
      dialog: false,
      search: '',
      isFormValid: true,
      headers: [
        {
          text: 'Naziv',
          value: 'naziv',
          sortable: true,
        },
        {
          text: 'Adresa',
          value: 'adresa',
          sortable: true,
        },
        {
          text: 'Grad',
          value: 'grad',
          sortable: true,
        },
        {
          text: 'Drzava',
          value: 'drzava',
          sortable: true,
        },
        {
          text: 'opis',
          value: 'data-table-expand',
        },
      ],

      newItem: {
        naziv: '',
        adresa: '',
        grad: '',
        drzava: '',
        opis: '',
      },
      update: false,

      //rules
      nazivRule: [
        v => !!v || 'Naziv je obavezan',
        v => (v && v.length <= 50) || 'Naziv ima najviše 50 karaktera'
      ],
      adresaRule: [
        v => !!v || 'Adresa je obavezna',
        v => (v && v.length <= 50) || 'Adresa ima najviše 50 karaktera'
      ],
      gradRule: [
        v => !!v || 'Grad je obavezan',
        v => (v && v.length <= 50) || 'Grad ima najviše 50 karaktera'
      ],
      // drzava je ostavljeno za autocomplete
      drzave: ['Afghanistan', 'Albania', 'Algeria', 'Andorra', 'Angola', 'Anguilla', 'Antigua &amp; Barbuda', 'Argentina', 'Armenia', 'Aruba', 'Australia', 'Austria', 'Azerbaijan', 'Bahamas', 'Bahrain', 'Bangladesh', 'Barbados', 'Belarus', 'Belgium', 'Belize', 'Benin', 'Bermuda', 'Bhutan', 'Bolivia', 'Bosnia &amp; Herzegovina', 'Botswana', 'Brazil', 'British Virgin Islands', 'Brunei', 'Bulgaria', 'Burkina Faso', 'Burundi', 'Cambodia', 'Cameroon', 'Cape Verde', 'Cayman Islands', 'Chad', 'Chile', 'China', 'Colombia', 'Congo', 'Cook Islands', 'Costa Rica', 'Cote D Ivoire', 'Croatia', 'Cruise Ship', 'Cuba', 'Cyprus', 'Czech Republic', 'Denmark', 'Djibouti', 'Dominica', 'Dominican Republic', 'Ecuador', 'Egypt', 'El Salvador', 'Equatorial Guinea', 'Estonia', 'Ethiopia', 'Falkland Islands', 'Faroe Islands', 'Fiji', 'Finland', 'France', 'French Polynesia', 'French West Indies', 'Gabon', 'Gambia', 'Georgia', 'Germany', 'Ghana', 'Gibraltar', 'Greece', 'Greenland', 'Grenada', 'Guam', 'Guatemala', 'Guernsey', 'Guinea', 'Guinea Bissau', 'Guyana', 'Haiti', 'Honduras', 'Hong Kong', 'Hungary', 'Iceland', 'India', 'Indonesia', 'Iran', 'Iraq', 'Ireland', 'Isle of Man', 'Israel', 'Italy', 'Jamaica', 'Japan', 'Jersey', 'Jordan', 'Kazakhstan', 'Kenya', 'Kuwait', 'Kyrgyz Republic', 'Laos', 'Latvia', 'Lebanon', 'Lesotho', 'Liberia', 'Libya', 'Liechtenstein', 'Lithuania', 'Luxembourg', 'Macau', 'Macedonia', 'Madagascar', 'Malawi', 'Malaysia', 'Maldives', 'Mali', 'Malta', 'Mauritania', 'Mauritius', 'Mexico', 'Moldova', 'Monaco', 'Mongolia', 'Montenegro', 'Montserrat', 'Morocco', 'Mozambique', 'Namibia', 'Nepal', 'Netherlands', 'Netherlands Antilles', 'New Caledonia', 'New Zealand', 'Nicaragua', 'Niger', 'Nigeria', 'Norway', 'Oman', 'Pakistan', 'Palestine', 'Panama', 'Papua New Guinea', 'Paraguay', 'Peru', 'Philippines', 'Poland', 'Portugal', 'Puerto Rico', 'Qatar', 'Reunion', 'Romania', 'Russia', 'Rwanda', 'Saint Pierre &amp; Miquelon', 'Samoa', 'San Marino', 'Satellite', 'Saudi Arabia', 'Senegal', 'Serbia', 'Seychelles', 'Sierra Leone', 'Singapore', 'Slovakia', 'Slovenia', 'South Africa', 'South Korea', 'Spain', 'Sri Lanka', 'St Kitts &amp; Nevis', 'St Lucia', 'St Vincent', 'St. Lucia', 'Sudan', 'Suriname', 'Swaziland', 'Sweden', 'Switzerland', 'Syria', 'Taiwan', 'Tajikistan', 'Tanzania', 'Thailand', "Timor L'Este", 'Togo', 'Tonga', 'Trinidad &amp; Tobago', 'Tunisia', 'Turkey', 'Turkmenistan', 'Turks &amp; Caicos', 'Uganda', 'Ukraine', 'United Arab Emirates', 'United Kingdom', 'United States', 'Uruguay', 'Uzbekistan', 'Venezuela', 'Vietnam', 'Virgin Islands (US)', 'Yemen', 'Zambia', 'Zimbabwe'],
      drzavaRule: [
        v => !!v || 'Drzava je obavezna',
        v => (v && v.length <= 50) || 'Drzava ima najviše 50 karaktera'
      ],

    };
  },

  computed: {

    ...mapGetters(
      {
        getAll: 'klinike/getKlinike',
        
        // get: 'sale/getSala'
      }
    ),
    
    
    formTitle: function(){
       return this.update ? 'Izmena klinike': 'Dodavanje nove klinike';
    },

  },
  created(){
    //load klinike
    this.fetchData();
    
  },
  methods: {
    ...mapActions(
      {
        fetchData: 'klinike/loadKlinike',
        addKlinika: 'klinike/addKlinika',
        updateKlinika: 'klinike/updateKlinikaFromAdminCentra',
        // removeSala: 'sale/removeSala'
      }
    ),


    resetNewItem(){
      this.newItem = {
        naziv: '',
        adresa: '',
        grad: '',
        drzava: '',
        opis: '',
      }
    },
    close(){
      this.resetNewItem();
      this.update = false;
      this.dialog = false;
    },
    save(){
      if(this.update){
        
        this.updateKlinika(this.newItem);
      }else{
        //adding klinika
        this.addKlinika(this.newItem);
      }
      this.close();
    },
    editItem(item){
      this.update = true;
      
      //this.newItem = Object.assign({}, item);
      this.newItem = {
        id: item.id,
        naziv: item.naziv,
        adresa: item.adresa,
        grad: item.grad,
        drzava: item.drzava,
        opis: item.opis,
      }
      this.dialog = true;
    },
  }
}
</script>

<style>

</style>