<template>
  <v-container fluid>
    <v-row justify="space-around">
      <v-col>
        <v-datetime-picker
          v-model="datetimeStart"
          label="Datum i vreme pocetka pregleda"
          dateFormat="dd.MM.yyyy"
          :datePickerProps="{min: new Date(new Date().getTime()+1000*60*60*24).toISOString()}"
          @input="setKraj()"
        >
          <template slot="actions" slot-scope="{ parent }">
            <v-btn color="error darken-1" @click="parent.clearHandler">Clear</v-btn>
            <v-btn color="success darken-1" @click="parent.okHandler">Done</v-btn>
          </template>
        </v-datetime-picker>
      </v-col>
      <v-col>
        <v-text-field
          v-model="datetimeEnd"
          label="Datum i vreme kraja pregleda"
          readonly
        ></v-text-field>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import {mapGetters} from 'vuex';
export default {
  name: "DatePicker",
  props: ["index"],
  data: function(){
    return{
      datetimeStart: null,
      datetimeEnd: null
    };
  },
  computed: {
    ...mapGetters({
      selektovaniTipPregleda: "pregledDialog/getTipPregleda"
    })
  },
  methods: {
    setKraj(){
      let obj = {
        index: this.index,
        done: this.datetimeStart != null
      }
      let temp = null;
      if(!obj.done){
        this.datetimeEnd = null;
      }else{
        temp = new Date(this.datetimeStart.getTime() + this.selektovaniTipPregleda.trajanjeMinuti*60000);
        let day = temp.getDate();
        let month = temp.getMonth() + 1;
        let hour = temp.getHours();
        let minute = temp.getMinutes();
        if((String(day)).length==1)
          day='0'+day;
        if((String(month)).length==1)
          month='0'+month;
        if((String(hour)).length==1)
          hour='0'+hour;
        if((String(minute)).length==1)
          minute='0'+minute;

        this.datetimeEnd=`${day}.${month}.${temp.getFullYear()} ${hour}:${minute}`;
      }
      this.$store.commit('pregledDialog/setPocetak', this.datetimeStart);
      this.$store.commit('pregledDialog/setKraj', temp);
      this.$emit('changeStatus', obj);
    }
  }
}
</script>

<style>

</style>