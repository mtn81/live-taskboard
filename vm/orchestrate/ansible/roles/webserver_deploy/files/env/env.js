var livetaskboard = {};
livetaskboard.app = {};
livetaskboard.app.webui = {};

console.log(window.location);

var host = window.location.host;

livetaskboard.app.webui.env = {
  authAccessApiEndpoint: '/api/auth-access',
  taskManageApiEndpoint: '/api/task-manage',
  widgetStoreApiEndpoint: '/api/widget-store',
  taskManageWsEndpoint: `ws://${host}/ws/task-manage`,
  widgetStoreWsEndpoint: `ws://${host}/ws/widget-store`
};
