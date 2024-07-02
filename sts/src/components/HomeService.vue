<template>
  <div class="layout">
    <Layout :style="{minHeight: '100vh'}">
      <Sider collapsible :collapsed-width="100" v-model="isCollapsed">
        <!-- 用户名显示区域 -->
        <div class="user-header">
          <span>{{ userName }}</span>
        </div>
        <!-- 菜单区域 -->
        <Menu active-name="1" theme="dark" width="auto" :class="menuitemClasses">
          <MenuItem name="1-1" @click="toggleMapVisibility">
            <Icon type="ios-navigate"></Icon>
            <span>游学推荐</span>
          </MenuItem>
          <MenuItem name="1-2" @click="toggleLog">
            <Icon type="md-bookmarks" />
            <span>游学日记</span>
          </MenuItem>
          <MenuItem name="1-3" @click="togglePerson">
            <Icon type="md-person" />
            <span>个性化</span>
          </MenuItem>
        </Menu>
      </Sider>
      <Layout>
        <Header>
        </Header>
        <Content>
          <Breadcrumb>
            <BreadcrumbItem>Home</BreadcrumbItem>
            <BreadcrumbItem>Components</BreadcrumbItem>
            <BreadcrumbItem>Layout</BreadcrumbItem>
          </Breadcrumb>
          <div v-if="!showMap && !showPerson && !showLog">
            <Card>
              <div>Content</div>
            </Card>
          </div>
          <Person v-if="showPerson" />
          <MapView v-if="showMap" />
          <Log v-if="showLog" />
        </Content>
      </Layout>
    </Layout>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import MapView from "@/components/MapContainer.vue"; // 确保路径正确
import Person from "@/components/PersonContainer.vue";
import Log from "@/components/PersonLog.vue";

export default {
  components: {
    MapView,
    Person,
    Log
  },
  data() {
    return {
      isCollapsed: false,
      showPerson: false,
      showMap: false,
      showLog: false
    };
  },
  computed: {
    menuitemClasses() {
      return [
        'menu-item',
        this.isCollapsed ? 'collapsed-menu' : '',
      ];
    },
    ...mapGetters([
      'userName'
    ])
  },
  methods: {
    toggleMapVisibility() {
      this.showMap = !this.showMap;
      this.showPerson = false; // 确保点击游学推荐时，Person组件不显示
      this.showLog = false;
    },
    togglePerson() {
      this.showPerson = !this.showPerson;
      this.showMap = false; // 确保点击个性化时，MapView组件不显示
      this.showLog = false;
    },
    toggleLog(){
      this.showLog = !this.showLog;
      this.showMap = false;
      this.showPerson = false;
    }
  }
};
</script>

<style scoped>
.layout {
  height: 100%;
}
.ivu-layout-header{
  background-color: #333333;
}
.user-header {
  padding: 15px 0;
  text-align: center;
  font-size: 20px;
  background: #333333;
  color: white; 
}

/* Header的样式 */
.header {
  background: none;
  box-shadow: none;
  padding: 0;
  height: 0; /* 或者保持为默认值，如果Header不需要显示任何内容 */
}

/* Content的样式 */
.content {
  padding: 0 16px 16px;
  padding-top: 0;
}

/* Breadcrumb的样式 */
.breadcrumb {
  margin: 20px 0;
}

.ivu-layout-sider {
  background-color: #333333;
}
/* <div class="ivu-layout-sider-trigger" style="width: 200px;"><i class="ivu-icon ivu-ico
  n-ios-arrow-back ivu-layout-sider-trigger-icon"></i></div> */
.ivu-layout-sider-trigger-icon{
  background-color: aqua;
}
.ivu-menu-item {
  background-color: #333333;
}

/* 其他样式保持不变 */
</style>