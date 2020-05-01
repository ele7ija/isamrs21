<template>
  <v-container fluid>
    <v-row justify="space-around">
      <v-col>
        <v-datetime-picker
          v-model="datetimeStart"
          label="Datum i vreme pocetka pregleda"
          dateFormat="dd.MM.yyyy"
          @input="clear()"
        >
          <template slot="actions" slot-scope="{ parent }">
            <v-btn color="error darken-1" @click="parent.clearHandler">Clear</v-btn>
            <v-btn color="success darken-1" @click="parent.okHandler">Done</v-btn>
          </template>
        </v-datetime-picker>
      </v-col>
      <v-col>
        <v-menu
          v-if="datetimeStart!=null"
          ref="menu"
          :close-on-content-click="false"
          :return-value.sync="datetimeEnd.time"
          v-model="datetimeEnd.menu2"
          max-width="290px"
          min-width="290px"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="datetimeEnd.time"
              label="Vreme kraja pregleda"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-time-picker
            v-if="datetimeEnd.menu2"
            v-model="datetimeEnd.time"
            label="Vreme kraja pregleda"
            dateFormat="dd.MM.yyyy"
            :min="`${datetimeStart.getHours()}:${datetimeStart.getMinutes()}`"
            @click:minute="enableContinue()"
          ></v-time-picker>
        </v-menu>
      </v-col>
    </v-row>
  </v-container>
</template>

<script> 
export default {
  name: "DatePicker",
  props: ["index"],
  data: function(){
    return{
      datetimeStart: null,
      datetimeEnd: {
        menu2: false,
        time: null,
      }
    };
  },
  methods: {
    enableContinue(){
      this.$refs.menu.save(this.datetimeEnd.time)
      let obj = {
        index: this.index,
        done: true
      }
      let tempDate = new Date(this.datetimeStart.getTime());
      tempDate.setHours(this.datetimeEnd.time.split(":")[0]);
      tempDate.setMinutes(this.datetimeEnd.time.split(":")[1]);
      console.log(tempDate);
      this.$store.commit('pregledDialog/setKraj', tempDate);
      this.$emit('changeStatus', obj);
    },
    clear(){
      this.datetimeEnd.time = null;
      let obj = {
        index: this.index,
        done: false
      }
      
      console.log(this.datetimeStart);
      this.$store.commit('pregledDialog/setPocetak', this.datetimeStart);
      
      this.$emit('changeStatus', obj);
    }
  }
}
</script>

<style>

</style>