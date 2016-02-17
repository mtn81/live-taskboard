var livetaskboard = {};
livetaskboard.app = {};
livetaskboard.app.webui = {};

livetaskboard.app.webui.env = {
  authAccessApiEndpoint: '/auth-access/api',
  taskManageApiEndpoint: '/task-manage/api',
  taskManageWsEndpoint: 'ws://localhost:28080/task-manage/websocket',
  widgetStoreApiEndpoint: '/widget-store/api',
  widgetStoreWsEndpoint: 'ws://localhost:38080/widget-store/websocket'
};
