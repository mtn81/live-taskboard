var livetaskboard = {};
livetaskboard.app = {};
livetaskboard.app.webui = {};

livetaskboard.app.webui.env = {
  authAccessApiEndpoint: '/api/auth-access',
  taskManageApiEndpoint: '/api/task-manage',
  taskManageWsEndpoint: 'ws://localhost:28080/task-manage/websocket',
  widgetStoreApiEndpoint: '/api/widget-store',
  widgetStoreWsEndpoint: 'ws://localhost:38080/widget-store/websocket'
};
