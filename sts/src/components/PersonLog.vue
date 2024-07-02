<template>
    <div>
      <!-- 搜索和表格部分 -->
      <div v-if="isRecommendationTabActive || isRecommendationsTabActive">
        <div class="flex flex-row justify-between">
          <div>
            <h1 class="font-bold">日志管理</h1>
          </div>
          <div>
            <label class="input input-bordered flex items-center gap-2">
              <input type="text" class="grow" placeholder="Search" v-model="searchQuery" @input="searchLogs" />
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" class="w-4 h-4 opacity-70">
                <path fill-rule="evenodd" d="M9.965 11.026a5 5 0 1 1 1.06-1.06l2.755 2.754a.75.75 0 1 1-1.06 1.06l-2.755-2.754ZM10.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z" clip-rule="evenodd" />
              </svg>
            </label>
          </div>
        </div>
        <!-- 日记总数显示 -->
        <div class="mt-4">
          <span class="text-lg">总日记数量: {{ totalLogsCount }}</span>
          <button @click="toggleRecommendations" class="btn btn-primary ml-4">
            {{ showRecommendations ? '显示所有日记' : '显示推荐日记' }}
          </button>
        </div>
      </div>
      <div role="tablist" class="tabs tabs-bordered">
        <input type="radio" name="my_tabs_1" role="tab" class="tab" aria-label="所有日记" v-model="activeTab" value="recommendation" @change="toggleRecommendations(false)" />
        <div v-if="isRecommendationTabActive || isRecommendationsTabActive" role="tabpanel" class="tab-content p-10">
          <div class="overflow-x-auto">
            <table class="table">
              <!-- head -->
              <thead>
                <tr>
                  <th></th>
                  <th>名称</th>
                  <th>主题</th>
                  <th>浏览量</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in filteredLogs" :key="index">
                  <td>{{ index + 1 }}</td>
                  <td>{{ item.diaryName }}</td>
                  <td>{{ item.location }}</td>
                  <td>{{ item.score }}</td>
                  <td class="btn btn-ghost" @click="viewLog(item)">查看</td>
                </tr>
              </tbody>
            </table>
          </div>
          <dialog ref="myDialog" class="modal">
            <div class="modal-box">
              <button @click="toggleModal" class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
              <h3 class="font-bold text-lg">{{ currentLog.diaryName }}</h3>
              <p class="py-4">{{ currentLog.content }}</p>
              <p class="mt-5">评分</p>
              <div class="rating">
                <input type="radio" name="rating-2" class="mask mask-star-2 bg-orange-400" />
                <input type="radio" name="rating-2" class="mask mask-star-2 bg-orange-400" checked />
                <input type="radio" name="rating-2" class="mask mask-star-2 bg-orange-400" />
                <input type="radio" name="rating-2" class="mask mask-star-2 bg-orange-400" />
                <input type="radio" name="rating-2" class="mask mask-star-2 bg-orange-400" />
              </div>
            </div>
          </dialog>
        </div>
  
        <input type="radio" name="my_tabs_1" role="tab" class="tab ml-2" aria-label="创建日记" v-model="activeTab" value="create" />
        <div v-if="isCreateTabActive" role="tabpanel" class="tab-content p-10">
          <form class="space-y-4" @submit.prevent="handleSubmit">
            <div>
              <label for="title" class="block text-sm font-medium text-gray-700">标题</label>
              <input type="text" id="title" v-model="newLog.diaryName" name="title" class="input input-bordered w-full" placeholder="请输入日记标题">
            </div>
            <div>
              <label for="scenicSpot" class="block text-sm font-medium text-gray-700">景点</label>
              <input type="text" id="scenicSpot" v-model="newLog.location" name="scenicSpot" class="input input-bordered w-full" placeholder="请输入景点名称">
            </div>
            <div>
              <label for="content" class="block text-sm font-medium text-gray-700">内容</label>
              <textarea id="content" v-model="newLog.content" name="content" rows="4" class="textarea textarea-bordered w-full h-[50vh]" placeholder="日记内容..."></textarea>
            </div>
            <div class="flex justify-center">
              <button type="submit" class="btn btn-wide">发布</button>
            </div>
          </form>
        </div>
      </div>
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
        showModal: false, // 控制显示和隐藏的状态
        searchQuery: '', // 搜索查询
        // logs: [], // 所有日记
        logs: [ ],
        recommendations: [], // 推荐日记
        newLog: {
          diaryName: '',
          content: '',
          authorName: "劳昊",
          location: '',

        },
        currentLog: {
          id: null,
          diaryName: '',
          content: '',
          authorName: '',
          authorId: '',
          location: '',
          score: '',

        },
        activeTab: 'recommendation', // 当前选中的标签页
        showRecommendations: false // 控制显示推荐日记
      };
    },
    computed: {
      filteredLogs() {
        const logs = this.showRecommendations ? this.recommendations : this.logs;
        if (this.searchQuery) {
          return logs.filter(log => log.diaryName.includes(this.searchQuery));
        }
        return logs;
      },
      totalLogsCount() {
        return this.logs.length;
      },
      isRecommendationTabActive() {
        return this.activeTab === 'recommendation';
      },
      isCreateTabActive() {
        return this.activeTab === 'create';
      },
      isRecommendationsTabActive() {
        return this.activeTab === 'recommendations';
      }
    },
    methods: {
      toggleModal() {
        this.showModal = !this.showModal;
      },
      viewLog(log) {
        log.score += 1; // 浏览量加1
        this.updateLog(log); // 更新后端日志数据
        this.currentLog = { ...log }; // 克隆日志对象到currentLog
        this.toggleModal();
      },
      handleSubmit() {
        console.log('新日记:', this.newLog);
        alert(this.newLog.authorName);
        // 向后端发送POST请求
        api.post('service/write-log', this.newLog)
          .then(response => {
            console.log('日志已保存:', response.data);
            alert('日志已保存');
            // 重置表单
            this.newLog = {
              diaryName: '',
              location: '',
              content: ''
            };
            this.fetchLogs(); // 重新获取所有日记
          })
          .catch(error => {
            console.error('保存日志时出错:', error);
            alert('保存日志时出错');
          });
      },
      updateLog(log) {
            api.post('service/update-log', log)
            this.fetchLogs(); // 更新所有日记
            this.fetchRecommendations(); // 更新推荐日记
      },
      searchLogs() {
        const logs = this.showRecommendations ? this.recommendations : this.logs;
        if (!this.searchQuery) {
          this.filteredLogs = logs;
          return;
        }
        api.post('/service/search-log', { query: this.searchQuery })
        .then(response => {
          if (this.showRecommendations) {
            this.recommendations = response.data;
          } else {
            this.logs = response.data;
          }
        })
        .catch(error => {
          console.error('搜索日志时出错:', error);
        });
      },
      toggleRecommendations() {
        this.showRecommendations = !this.showRecommendations;
        this.searchLogs();
      },
      fetchLogs() {
      api.post('/service/all-log')
        .then(response => {
          this.logs = response.data;
        })
        .catch(error => {
          console.error('获取所有日志时出错:', error);
        });
    },
    fetchRecommendations() {
      api.post('/service/re-log', {
        authorName: "劳昊"
      })
        .then(response => {
          this.recommendations = response.data;
        })
        .catch(error => {
          console.error('获取推荐日志时出错:', error);
        });
    }
  },
  created() {
    // this.newLog.authorName = "劳昊";
    this.fetchLogs(); // 加载组件时获取所有日志
    this.fetchRecommendations(); // 加载组件时获取推荐日志
  },
  watch: {
    showModal(newValue) {
      if (newValue) {
        this.$refs.myDialog.showModal();
      } else {
        this.$refs.myDialog.close();
      }
    }
  }
};
</script>

<style scoped>
dialog {
  position: fixed; /* 固定定位 */
  top: 50%; /* 移到视口中央 */
  left: 50%; /* 移到视口中央 */
  transform: translate(-50%, -50%); /* 精确居中 */
  width: 50%; /* 宽度自定 */
  border: none;
  border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.5);
  padding: 20px;
}
</style>

  