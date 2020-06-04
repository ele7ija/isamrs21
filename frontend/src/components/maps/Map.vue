<template>
  <MglMap
    v-if="klinika"
    :accessToken="accessToken"
    :center="center"
    :mapStyle="mapStyle"
    :renderWorldCopies="false"
    :maxBounds="maxBounds"
    :zoom="17"
    ref="map">
    <MglMarker
      :coordinates="getKoordinate(klinika.lokacija)"
      :draggable="false"
    >
    </MglMarker>
  </MglMap>
</template>

<script>
import Mapbox from "mapbox-gl";
import { MglMap, MglMarker } from "vue-mapbox";
export default {
  name: 'Map',
  props: ["klinika"],
  components: {
    MglMap,
    MglMarker,
  },
  data() {
    return {
      accessToken: 'pk.eyJ1IjoibWlsYW5kanVyaWMiLCJhIjoiY2thOXZqc21pMHFwbjJ4bzM3bTA1YnA0eiJ9.qqO9dfeSglw9NL03P05LRQ',
      mapStyle: 'mapbox://styles/mapbox/streets-v11',
      maxBounds: [
        [-180,-85], [180,85] 
        //[17, 41.7], [25, 46.3] // srbija
        //[-30, 32], [70, 72] // evropa
      ],
      focusedEntity: null,
      dragStartLatitude: null,
      dragStartLongitude: null,
    };
  },
  

  created() {
    // We need to set mapbox-gl library here in order to use it in template
    this.mapbox = Mapbox;
  },

  computed:{
    center(){
      return this.getKoordinate(this.klinika.lokacija);
    }
  },

  methods:{
    getKoordinate(koordinate){
      console.log(koordinate);
      return [koordinate.geografskaDuzina, koordinate.geografskaSirina];
    }
  }
};
</script>

<style scoped>

</style>