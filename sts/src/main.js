import { createApp } from 'vue';
import ViewUIPlus from 'view-ui-plus';
import App from './App.vue';
import router from './router.js';
import store from './store'; 
import 'view-ui-plus/dist/styles/viewuiplus.css';


screenX
const app = createApp(App);
app.use(router);
app.use(store);
app.use(ViewUIPlus);
app.mount('#app');

