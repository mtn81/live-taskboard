export function aaApi(path) {
  return livetaskboard.app.webui.env.authAccessApiEndpoint + path;
}
export function tmApi(path) {
  return livetaskboard.app.webui.env.taskManageApiEndpoint + path;
}
export function tmWebSocket(path) {
  return livetaskboard.app.webui.env.taskManageWsEndpoint + path;
}
export function wsApi(path) {
  return livetaskboard.app.webui.env.widgetStoreApiEndpoint + path;
}
export function wsWebSocket(path) {
  return livetaskboard.app.webui.env.widgetStoreWsEndpoint + path;
}
