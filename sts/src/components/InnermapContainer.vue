<template>
  <div id="map-container">
    <div id="control-panel">
      <button @click="togglePlanner" id="toggle-button">{{ showPlanner ? '<<' : '>>' }}</button>
      <button @click="exportCSV" id="export-button">导出CSV</button>
    </div>
    <div :class="['route-planner', { hidden: !showPlanner }]">
      <input v-model="startLocation" placeholder="始发地">
      <span class="arrow">+</span>
      <div v-for="(waypoint, index) in waypoints" :key="index" class="waypoint-container">
        <input v-model="waypoints[index]" placeholder="途径点">
        <span class="arrow">+</span>
      </div>
      <input v-model="endLocation" placeholder="目的地">
      <div class="button-container">
        <select v-model="selectedRouteOption">
          <option value="distance">按距离规划</option>
          <option value="time">按时间规划</option>
        </select>
        <button @click="planRoute">规划路线</button>
        <div class="waypoint-controls">
          <button @click="addWaypoint">+</button>
          <button @click="removeWaypoint">-</button>
        </div>
      </div>
    </div>
    <div id="search-box">
      <input v-model="searchQuery" @keyup="searchPlace" placeholder="搜索场所">
      <ul v-if="searchResults.length">
        <li v-for="(result, index) in searchResults" :key="index" @click="selectPlace(result)">
          {{ result.name }}
        </li>
      </ul>
      <button @click="searchPlace">搜索</button>
    </div>
    <div id="map"></div>
    <div v-if="showNearbyPlaces" id="nearby-places">
      <h3>周围的地点</h3>
      <ul>
        <li v-for="(place, index) in nearbyPlaces" :key="index">{{ place.name }}</li>
      </ul>
    </div>
  </div>
</template>

<script>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import api from '@/api';
import { saveAs } from 'file-saver';
import customMarkerIcon from '@/assets/custom-marker-icon.png';
import customMarkerIcon2 from '@/assets/custom-marker-icon2.png';

export default {
  name: 'CampusMap',
  data() {
    return {
      map: null,
      startLocation: '',
      endLocation: '',
      waypoints: [],
      searchQuery: '',
      searchResults: [],
      markers: [],
      routeLayer: null,
      showPlanner: true,
      locations: [],
      showNearbyPlaces: false,
      nearbyPlaces: [],
      selectedRouteOption: 'distance'
    };
  },

  mounted() {
    this.initMap();
  },

  methods: {
    initMap() {
      this.map = L.map('map').setView([39.9603, 116.352], 17);

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      }).addTo(this.map);

      const campusMapImageUrl = require('@/assets/bupt.png');
      const nwCorner = L.latLng(39.9565, 116.3485);
      const neCorner = L.latLng(39.9637, 116.3549);
      const swCorner = L.latLng(39.9565, 116.349);
      const seCorner = L.latLng(39.9637, 116.3549);
      const imageBounds = L.latLngBounds([nwCorner, neCorner, swCorner, seCorner]);

      L.imageOverlay(campusMapImageUrl, imageBounds).addTo(this.map);

      this.map.on('click', (e) => {
        const lat = e.latlng.lat.toFixed(6);
        const lng = e.latlng.lng.toFixed(6);
        const locationName = prompt("请输入该地点的名称:", "");
        if (locationName) {
          this.locations.push({ id: this.locations.length + 1, name: locationName, latitude: lat, longitude: lng });
          alert(`地点: ${locationName}, 纬度: ${lat}, 经度: ${lng}`);
        }
      });
    },

    addMarkers(locations) {
      const customIcon = L.icon({
        iconUrl: customMarkerIcon,
        iconSize: [120, 120],
        iconAnchor: [60, 60],
        popupAnchor: [0, -60],
        shadowSize: [120, 120],
        shadowAnchor: [60, 60]
      });

      locations.forEach(location => {
        const marker = L.marker(location.coords, { icon: customIcon }).addTo(this.map);
        marker.bindPopup(`<strong>${location.name}</strong>`);
        this.markers.push(marker);
      });
    },

    addMarkers2(locations) {
      const customIcon = L.icon({
        iconUrl: customMarkerIcon2,
        iconSize: [120, 120],
        iconAnchor: [60, 60],
        popupAnchor: [0, -60],
        shadowSize: [120, 120],
        shadowAnchor: [60, 60]
      });
      locations.forEach(location => {
        const marker = L.marker(location.coords, { icon: customIcon }).addTo(this.map);
        marker.bindPopup(`<strong>${location.name}</strong>`);
        this.markers.push(marker);
      });
    },

    async searchPlace() {
      try {
        const response = await api.post('/service/search-place', {
          query: this.searchQuery
        });

        this.searchResults = response.data;
      } catch (error) {
        console.error('搜索场所出错:', error);
      }
    },

    selectPlace(place) {
      this.markers.forEach(marker => this.map.removeLayer(marker));
      this.markers = [];

      this.addMarkers([{ name: place.name, coords: [place.latitude, place.longitude] }]);

      this.map.setView([place.latitude, place.longitude], 17);

      this.searchResults = [];
      this.searchQuery = '';

      this.fetchNearbyPlaces(place.latitude, place.longitude, place.id);
    },

    async fetchNearbyPlaces(lat, lng, id) {
      try {
        const response = await api.post('/service/nearby-places', {
          latitude: lat,
          longitude: lng,
          id: id,
        });
        this.nearbyPlaces = response.data;
        this.showNearbyPlaces = true;
      } catch (error) {
        console.error('获取周围地点时出错:', error);
      }
    },

    addWaypoint() {
      this.waypoints.push('');
    },

    removeWaypoint() {
      if (this.waypoints.length > 0) {
        this.waypoints.pop();
      }
    },

    async planRoute() {
      if (this.routeLayer) {
        this.map.removeLayer(this.routeLayer);
      }
      try {
        let response;
        if (this.selectedRouteOption === 'distance') {
          response = await api.post('/service/plan-route', {
            start: this.startLocation,
            end: this.endLocation,
            waypoints: this.waypoints
          });
        } else if (this.selectedRouteOption === 'time') {
          response = await api.post('/service/plan-route-time', {
            start: this.startLocation,
            end: this.endLocation,
            waypoints: this.waypoints
          });
        }

        const route = response.data;
        const routeCoords = route.map(point => [point.latitude, point.longitude]);

        this.routeLayer = L.polyline(routeCoords, { color: 'blue' }).addTo(this.map);
        this.map.fitBounds(this.routeLayer.getBounds());
      } catch (error) {
        console.error('规划路线出错:', error);
      }
    },

    togglePlanner() {
      this.showPlanner = !this.showPlanner;
    },

    exportCSV() {
      if (this.locations.length === 0) {
        alert("没有可导出的地点信息！");
        return;
      }

      let csvContent = "id,location,latitude,longitude\n";
      this.locations.forEach(location => {
        csvContent += `${location.id},${location.name},${location.latitude},${location.longitude}\n`;
      });

      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      saveAs(blob, 'bupt_locations.csv');
    }
  }
}
</script>
<style scoped>
#map-container {
  position: relative;
  width: 100%;
  height: 100vh;
}

#control-panel {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 1000;
  display: flex;
  gap: 10px;
}

#toggle-button,
#export-button {
  background-color: rgb(247, 241, 241);
  border: 1px solid #ccc;
  padding: 10px;
  cursor: pointer;
  box-shadow: 0
  2px 10px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  font-weight: bold;
}

.route-planner {
  position: absolute;
  top: 80px;
  left: 10px;
  z-index: 1000;
  background-color: white;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  transition: transform 0.3s ease;
}

.route-planner.hidden {
  transform: translateX(-100%);
}

.waypoint-container {
  display: flex;
  align-items: center;
}

.route-planner .arrow {
  margin: 0 10px;
  font-size: 20px;
}

.route-planner .button-container {
  display: flex;
  align-items: center;
  margin-left: 10px;
}

.route-planner .waypoint-controls button {
  margin-left: 5px;
  padding: 5px;
}

#search-box {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 1000;
  background-color: white;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
}

#search-box ul {
  position: absolute;
  top: 43px;
  right: 60px;
  width: 175px;
  max-height: 200px;
  overflow-y: auto;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 1001;
  padding: 0;
  margin: 0;
  list-style: none;
}

#search-box ul li {
  padding: 10px;
  cursor: pointer;
}

#search-box ul li:hover {
  background-color: #f0f0f0;
}

#map {
  width: 100%;
  height: 100%;
}

input {
  border: 1px solid #ccc;
  padding: 5px;
  margin-right: 5px;
}

#search-box input {
  margin-right: 5px;
  width: 180px;
}

#search-box button {
  padding: 5px;
  border: 1px solid #ccc;
}

.route-planner input {
  width: 150px;
}

.route-planner button {
  padding: 5px;
  border: 1px solid #ccc;
}

#nearby-places {
  position: absolute;
  top: 150px;
  left: 10px;
  z-index: 1000;
  background-color: white;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  max-height: 300px;
  overflow-y: auto;
}

#nearby-places h3 {
  margin: 0 0 10px;
}

#nearby-places ul {

  list-style: none;
  padding: 0;
  margin: 0;
}

#nearby-places ul li {
  padding: 5px 0;
  border-bottom: 1px solid #eee;
}
</style>
