<template>
<div class="pa-3">
  <v-row class="fill-height">
    <v-col>
      <v-sheet height="64">
        <v-toolbar flat color="white">
          <v-btn outlined class="mr-4" color="grey darken-2" @click="setToday">
            Danas
          </v-btn>
          <v-btn fab text small color="grey darken-2" @click="prev">
            <v-icon small>mdi-chevron-left</v-icon>
          </v-btn>
          <v-btn fab text small color="grey darken-2" @click="next">
            <v-icon small>mdi-chevron-right</v-icon>
          </v-btn>
          
          <!-- datumi -->
          <v-toolbar-title>{{ title }}</v-toolbar-title>
          <!-- drop down meni za mesec dan itd -->
          <v-spacer></v-spacer>
          <v-menu bottom right>
            <template v-slot:activator="{ on }">
              <v-btn
                outlined
                color="grey darken-2"
                v-on="on">
                <span>{{ typeToLabel[type] }}</span>
                <v-icon right>mdi-menu-down</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item @click="type = 'day'">
                <v-list-item-title>Dan</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = 'week'">
                <v-list-item-title>Nedelja</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = 'month'">
                <v-list-item-title>Mesec</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-toolbar>
      </v-sheet>

      <!-- sam kalendar -->
      <v-sheet height="500">
        <v-calendar
          ref="calendar"
          v-model="focus"
          color="info"
          :events="events"
          :event-color="getEventColor"
          :now="today"
          :type="type"
          :weekday-format="daniUNedelji"
          @click:event="showEvent"
          @click:more="viewDay"
          @click:date="viewDay"
          @change="updateRange"
        ></v-calendar>
        <v-menu
          v-model="selectedOpen"
          :close-on-content-click="false"
          :activator="selectedElement"
          offset-x
        >
          <v-card
            color="grey lighten-4"
            min-width="350px"
            flat>
            <v-toolbar
              :color="selectedEvent.color"
              dark>
              <v-toolbar-title> {{selectedEvent.name}}</v-toolbar-title>
            </v-toolbar>
            <v-card-text>
              pocetak u: {{selectedEvent.start}}
            </v-card-text>
            <v-card-text>
              kraj u: {{selectedEvent.end}}
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn text color="secondary"  @click="selectedOpen = false">
                Zatvori
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-menu>
      </v-sheet>
    </v-col>
  </v-row>
</div>
</template>

<script>

export default {
  props: ["events", "today"],

  data: () => ({
    focus: '',
    type: 'month',
    typeToLabel: {
      day: 'Dan',
      month: 'Mesec',
      week: 'Nedelja',
    },
    start: null,
    end: null,
    selectedEvent: {},
    selectedElement: null,
    selectedOpen: false,
    colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'grey darken-1'],
    names: ["Kardioloski pregled", "Gastroenteroloski"],
  }),
  computed: {
    title () {
      const { start, end } = this
      if (!start || !end) {
        return ''
      }
      // options = {weekday: "long", year: "numeric", month: "long", day: "numeric"};
      var datum = new Date(start.date);
      var mesec = datum.toLocaleString("sr-Latn-RS", {month: "long"});
      var godina = datum.toLocaleString("sr-Latn-RS", {year: "numeric"});
      var dan = datum.toLocaleString("sr-Latn-RS", {day: "numeric"});
    
      //end datum
      var endMesec = new Date(end.date).toLocaleString("sr-Latn-RS", {month: "long"});
      var endDan = new Date(end.date).toLocaleString("sr-Latn-RS", {day: "numeric"});

      switch (this.type) {
        case 'month':
          return `${mesec} ${godina}`;
        case 'week':
          return `${dan} ${mesec} - ${endDan} ${endMesec}`;
        case 'day':
          return `${dan} ${mesec} ${godina}`;
      }
      return ''
    },
  },
  mounted () {
    this.$refs.calendar.checkChange()
  },
  methods: {
    //my methods
    daniUNedelji(day){
      var WeekDayString = new Date(day.date).toLocaleString("sr-Latn-RS", {weekday: "long"});
      return WeekDayString;
    },

//vuetify methods
    viewDay ({ date }) {
      this.focus = date
      this.type = 'day'
    },
    getEventColor (event) {
      return event.color
    },
    setToday () {
      this.focus = this.today
    },
    prev () {
      this.$refs.calendar.prev()
    },
    next () {
      this.$refs.calendar.next()
    },
    showEvent ({ nativeEvent, event }) {
      const open = () => {
        this.selectedEvent = event
        this.selectedElement = nativeEvent.target
        setTimeout(() => this.selectedOpen = true, 10)
      }

      if (this.selectedOpen) {
        this.selectedOpen = false
        setTimeout(open, 10)
      } else {
        open()
      }
      nativeEvent.stopPropagation();
    },
    updateRange ({ start, end }) {
      this.start = start
      this.end = end        
    },
  },
}
</script>