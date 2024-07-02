<template>
  <div class="search-container" @click="hideSuggestionsIfNotFocused">
    <input
      ref="searchInput"
      v-model="searchQuery"
      @focus="showRecommendations"
      @blur="hideSuggestions"
      @keyup.up.prevent="highlightPrevious"
      @keyup.down.prevent="highlightNext"
      @keyup.enter.prevent="selectHighlighted"
      placeholder="输入地点进行搜索"
    />
    <button @click="searchPlace">搜索</button>
    <button @click="goToScenicArea">进入景区</button>
    <ul v-if="isSuggestionsVisible" class="suggestions-list">
      <li
        v-for="(suggestion, index) in suggestions"
        :key="index"
        :class="{ active: index === highlightedIndex }"
        @click="selectSuggestion(suggestion)"
        @keyup.enter.prevent="selectSuggestion(suggestion)"
        tabindex="0"
        class="suggestion-item"
      >
        {{ suggestion }}
      </li>
    </ul>
    <div id="container"></div>
  </div>
</template>

<script>
import AMapLoader from "@amap/amap-jsapi-loader";
import api from '@/api';
import { mapGetters } from 'vuex';

export default {
  name: "map-view",
  computed: {
    ...mapGetters([
      'userName'
    ])
  },
  data() {
    return {
      searchQuery: '',
      suggestions: [],
      highlightedIndex: -1,
      isSuggestionsVisible: false,
      userName: "劳昊",
      locationsMap: 
      {
  "故宫": {"lng": 116.397128, "lat": 39.916345},
  "颐和园": {"lng": 116.272886, "lat": 39.999371},
  "八达岭长城": {"lng": 116.005776, "lat": 40.359912},
  "明十三陵": {"lng": 116.220301, "lat": 40.253385},
  "天坛": {"lng": 116.413374, "lat": 39.882334},
  "北海公园": {"lng": 116.396251, "lat": 39.929501},
  "恭王府": {"lng": 116.389921, "lat": 39.937456},
  "798艺术区": {"lng": 116.49564, "lat": 39.984976},
  "中国国家博物馆": {"lng": 116.40738, "lat": 39.903555},
  "北京动物园": {"lng": 116.339125, "lat": 39.938133},
  "北京植物园": {"lng": 116.204858, "lat": 40.009588},
  "南锣鼓巷": {"lng": 116.40394, "lat": 39.933565},
  "香山公园": {"lng": 116.186327, "lat": 39.998279},
  "圆明园": {"lng": 116.303709, "lat": 40.007995},
  "潭柘寺": {"lng": 115.95235, "lat": 39.893432},
  "白塔寺": {"lng": 116.379348, "lat": 39.926157},
  "中央电视塔": {"lng": 116.307653, "lat": 39.92354},
  "北京大学": {"lng": 116.305434, "lat": 39.988475},
  "清华大学": {"lng": 116.326851, "lat": 40.003313},
  "中国人民大学": {"lng": 116.320245, "lat": 39.967405},
  "鸟巢": {"lng": 116.396755, "lat": 39.991957},
  "水立方": {"lng": 116.387194, "lat": 39.994913},
  "国子监": {"lng": 116.416309, "lat": 39.947277},
  "北京首都博物馆": {"lng": 116.346278, "lat": 39.91349},
  "颐和园后湖": {"lng": 116.276619, "lat": 39.996582},
  "昌平温都水城": {"lng": 116.241244, "lat": 40.205859},
  "怀柔红螺寺": {"lng": 116.627662, "lat": 40.365532},
  "北京海洋馆": {"lng": 116.347026, "lat": 39.942728},
  "北京天文馆": {"lng": 116.342804, "lat": 39.940679},
  "大运河森林公园": {"lng": 116.674372, "lat": 39.914127},
  "东郊森林公园": {"lng": 116.577748, "lat": 39.988171},
  "石景山游乐园": {"lng": 116.222982, "lat": 39.914944},
  "中央电视台大楼": {"lng": 116.461007, "lat": 39.914939},
  "潘家园旧货市场": {"lng": 116.463347, "lat": 39.876019},
  "北京欢乐谷": {"lng": 116.490846, "lat": 39.874774},
  "北京中华民族园": {"lng": 116.393295, "lat": 39.974654},
  "北大荒之旅": {"lng": 116.395645, "lat": 39.928752},
  "万里长城": {"lng": 115.977111, "lat": 40.347435},
  "司马台长城": {"lng": 117.282196, "lat": 40.661084}
},
      recommendations: []
    };
  },
  methods: {
    // async searchPlace() {
    //   // 模拟的后端返回数据
    //   // const results = [];
      
    //   // const results = await this.searchLocations(this.searchQuery);
    //   const selectedLocation = this.locationsMap[this.searchQuery];
    //   if (selectedLocation) {
    //     this.map.setCenter(new AMap.LngLat(selectedLocation.lng, selectedLocation.lat));
    //     this.map.setZoom(16);
    //   } else {
    //     alert('没有找到地点的经纬度信息');
    //   }
    //   if (results && results.length > 0) {
    //     this.suggestions = results;
    //     this.isSuggestionsVisible = true;
    //   } else {
    //     alert('没有找到地点');
    //   }
    // },
    async searchPlace() {
      // 调用后端 API 搜索地点
      try {
        const response = await api.post('/service/search-loc', {
          query: this.searchQuery
        });
        const results = response.data;
        
        if (results && results.length > 0) {
          this.suggestions = results;
          this.isSuggestionsVisible = true;
        } else {
          alert('没有找到匹配的地点');
        }
        
        const selectedLocation = this.locationsMap[this.searchQuery];
        if (selectedLocation) {
          this.map.setCenter(new AMap.LngLat(selectedLocation.lng, selectedLocation.lat));
          this.map.setZoom(16);
        } else {
          alert('没有找到地点的经纬度信息');
        }
      } catch (error) {
        console.error('搜索地点时出错:', error);
        alert('搜索地点时出错');
      }
    },
    goToScenicArea() {
    this.$router.push({
      name: 'Map',
      params: { place: this.searchQuery }
    });
  },
    showRecommendations() {
      if (!this.searchQuery) {
        this.suggestions = this.recommendations;
      }
      this.isSuggestionsVisible = true;
    },
    hideSuggestions() {
      this.isSuggestionsVisible = false;
      this.highlightedIndex = -1;
    },
    hideSuggestionsIfNotFocused(event) {
      const searchInput = this.$refs.searchInput;
      if (searchInput && !searchInput.contains(event.target)) {
        this.hideSuggestions();
        this.suggestions = this.recommendations; // 重新显示推荐
      }
    },
    highlightPrevious() {
      if (this.highlightedIndex > 0) {
        this.highlightedIndex--;
      }
    },
    highlightNext() {
      if (this.highlightedIndex < this.suggestions.length - 1) {
        this.highlightedIndex++;
      }
    },
    selectHighlighted() {
      if (this.highlightedIndex >= 0 && this.highlightedIndex < this.suggestions.length) {
        this.searchQuery = this.suggestions[this.highlightedIndex];
        this.selectSuggestion(this.searchQuery);
      }
    },
    selectSuggestion(suggestion) {
      this.searchQuery = suggestion;

      this.hideSuggestions();
    },
    fetchRecommendations() {
      api.post('/service/recommendations', { username: this.userName })
        .then(response => {
          this.recommendations = response.data.map(location => location.name);
          this.suggestions = this.recommendations; // 初始化时显示推荐
          this.isSuggestionsVisible = false;
        })
        .catch(error => {
          console.error('获取推荐地点失败:', error);
        });
    },
    initAMap() {
      AMapLoader.load({
        key: "81de5fced9699d1acfd58de8710f1adf",
        version: "2.0",
        plugins: ["AMap.PlaceSearch"],
      })
      .then((AMap) => {
        this.map = new AMap.Map("container", {
          viewMode: "3D",
          zoom: 11,
          center: [116.397428, 39.90923],
        });
        this.placeSearch = new AMap.PlaceSearch({
          pageSize: 5,
          pageIndex: 1,
          citylimit: true,
          autoFitView: true,
          city: "北京",
          map: this.map,
        });
      })
      .catch((e) => {
        console.log(e);
      });
    },
  },
  mounted() {
    this.fetchRecommendations();
    document.addEventListener('click', this.hideSuggestionsIfNotFocused);
    this.initAMap();
  },
  unmounted() {
    document.removeEventListener('click', this.hideSuggestionsIfNotFocused);
    this.map?.destroy();
  },
};
</script>

<style scoped>
#container {
  width: 100%;
  height: calc(100vh - 50px); /* 减去搜索框的高度，假设搜索框高度为50px */
  position: relative; /* 使top属性生效 */
  top: 50px; /* 搜索框的高度 */
}
input[type="text"] {
  /* 输入框的样式 */
  width: 50%;
  padding: 10px;
  margin-bottom: 10px; /* 输入框与地图之间的间隔 */
  box-sizing: border-box; /* 边框计算在宽度内 */
  background-color: #fff;
  color: #fff;
}

button {
  /* 按钮的样式 */
  border: 1px solid #ccc; /* 增加边框 */
  /* margin-right: 1px; */
  margin-left: 25px;
  padding: 3px;
}
.search-container {
  position: relative; /* 使建议列表相对于这个容器定位 */
  max-width: 100%;
}

.suggestions-list {
  position: absolute;
  z-index: 1000;
  left: 38.1%;
  width: 12.5%;
  background: #555555;
  color: #fff;
  border: 1px solid #ccc;
  border-top: none;
  list-style: none;
  padding: 0;
  margin: 0;
  max-height: 200px; /* 限制下拉列表的最大高度 */
  overflow-y: auto;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.suggestion-item.active {
  background-color: #b8b6b6; /* 高亮状态下的背景色 */
}

.suggestions-list li {
  padding: 10px;
  cursor: pointer;
}
</style>
