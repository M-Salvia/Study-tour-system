<template>
  <div class="profile-editor mx-300 flex flex-col">
    <h1 class="font-bold">用户偏好</h1>
    *调整下方的几项数据以影响您的景点推荐顺序
    <form class="pt-10" @submit.prevent="handleSubmit">
      <div class="ratings flex flex-col">
        <label v-for="(value, key) in profile.preferences" :key="key">
          {{ labels[key] }}
          <input :disabled="!isEditable" type="range" min="0" max="100" v-model="profile.preferences[key]" class="range w-1/3 mb-5" />
          {{ value }}
        </label>
      </div>
      <div class="flex-row space-x-10">
        <button type="button" @click="toggleEdit" class="btn">{{ isEditable ? '取消' : '编辑' }}</button>
        <button type="submit" :disabled="!isEditable" class="btn">保存</button>
      </div>
    </form>
  </div>
</template>

<script>
import api from '@/api';
import { mapGetters } from 'vuex';
export default {
  computed: {
    ...mapGetters([
      'userName'
    ])
  },
  data() {
    return {
      isEditable: false,
      profile: {
        username: '{userName}',
        preferences: {
          traffic: 50,
          serviceQuality: 50,
          touristFlow: 50,
          costPerformance: 50,
          culturalAtmosphere: 50,
          naturalScenery: 50,
        }
      },
      labels: {
        traffic: "交通便利度",
        serviceQuality: "服务质量",
        touristFlow: "游客流量",
        costPerformance: "性价比",
        culturalAtmosphere: "人文气息",
        naturalScenery: "自然风光",
      }
    };
  },
  created() {
    // 从 Vuex 获取用户名并初始化 profile.username
    this.profile.username = this.userName;
  },
  methods: {
    async handleSubmit() {
      alert(this.profile.username);
      try{
        api.post('/service/person', {
        username: this.profile.username,
        preferences: {
            trafficConvenience: this.profile.preferences.traffic,
            serviceQuality: this.profile.preferences.serviceQuality,
            visitorFlowRate: this.profile.preferences.touristFlow,
            pricePerformanceRatio: this.profile.preferences.costPerformance,
            culturalAtmosphere: this.profile.preferences.culturalAtmosphere,
            naturalLandscape: this.profile.preferences.naturalScenery
          }
      }, {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      });
        console.log('Preferences updated successfully');
        this.isEditable = false;  // 保存后禁用编辑模式
        alert("保存成功");
      }
      catch (error) {
        console.error('Failed to update preferences:', error);
        this.$Modal.error({
          title: '保存失败',
          content: '发生未知网络错误，请稍后再试',
        });
      }
    
    },
    toggleEdit() {
      this.isEditable = !this.isEditable;  // 切换编辑状态
    }
  }
};
</script>

<!-- <style scoped>
@tailwind base;
@tailwind components;
@tailwind utilities;
</style> -->
