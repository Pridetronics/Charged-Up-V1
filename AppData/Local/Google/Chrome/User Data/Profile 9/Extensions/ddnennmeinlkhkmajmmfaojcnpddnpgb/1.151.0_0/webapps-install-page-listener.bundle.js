!function(e){var t={};function r(o){if(t[o])return t[o].exports;var n=t[o]={i:o,l:!1,exports:{}};return e[o].call(n.exports,n,n.exports,r),n.l=!0,n.exports}r.m=e,r.c=t,r.d=function(e,t,o){r.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:o})},r.r=function(e){"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},r.t=function(e,t){if(1&t&&(e=r(e)),8&t)return e;if(4&t&&"object"==typeof e&&e&&e.__esModule)return e;var o=Object.create(null);if(r.r(o),Object.defineProperty(o,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var n in e)r.d(o,n,function(t){return e[t]}.bind(null,n));return o},r.n=function(e){var t=e&&e.__esModule?function(){return e.default}:function(){return e};return r.d(t,"a",t),t},r.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},r.p="",r(r.s=122)}({1:function(e,t,r){"use strict";var o=r(3);t.a=new class{constructor(e){this.isProduction=!1,this.productName="Unknown Product",this.version="1.0",this.supportedPlatforms=[["win","x86-32"],["win","x86-64"],["mac","x86-64"],["mac","arm64"]],this.teChromeInstallUrl="https://chrome-install.url",this.errorReportingURL="https://error-reporting.url",this.nativeChannelApplicationName="applicationNamespace",this.nativeChannelMaxRelaunchIntervalMs=1e4,this.helperIdleTimeoutMs=3e4,this.sensitiveHeaders=["set-cookie","cookie","authorization"],this.postLoadEntriesBufferTimeMs=2e3,this.pageNavigationTimeoutMs=6e4,this.requestTimeoutMs=3e5,this.geolocationPollInterval=3e5,this.geolocationRequestTimeoutMs=5e3,this.helperStatusQueryName="getHelperStatus",this.popupClickedMessageType="popupClicked",this.triggerStartUrl="fakeTriggerStartUrl",this.pageUserInactivityTimeoutMs=3e4,this.connectChromeHelperRetryDelayMs=3e4,this.logDumpBufferSize=100,this.errorReportLogMessageCount=20,this.logReportIntervalMs=3e4,this.checkinIntervalMs=6e4,Object.assign(this,e)}}({isProduction:!0,productName:o.TE_CHROMIUM_PRODUCT,version:o.TE_VERSION,teChromeInstallUrl:o.TE_CHROME_INSTALL_URL,errorReportingURL:`${o.TE_CONTROL_API_ENDPOINT}/eyebrow/${o.TE_CONTROL_API_PRODUCT_PATH}/browser-extension-error`,nativeChannelApplicationName:o.TE_CHROMIUM_NATIVE_MESSAGING_HOST,nativeChannelMaxRelaunchIntervalMs:1e4,helperIdleTimeoutMs:3e4,postLoadEntriesBufferTimeMs:2e3,pageNavigationTimeoutMs:6e4,requestTimeoutMs:3e5,geolocationPollInterval:3e5,helperStatusQueryName:"getHelperStatus",popupClickedMessageType:"popupClicked",triggerStartUrl:chrome.runtime.getURL("views/trigger-start.html"),pageUserInactivityTimeoutMs:3e4,connectChromeHelperRetryDelayMs:3e4,logDumpBufferSize:100,errorReportLogMessageCount:20,logReportIntervalMs:3e4,checkinIntervalMs:6e4})},122:function(e,t,r){"use strict";r.r(t);var o=r(1);const n={brokerState:"connected"},s={brokerState:"disconnected"},i={sender:"te-eyebrow-chrome-extension",eyebrowId:chrome.runtime.id,eyebrowVersion:chrome.runtime.getManifest().version,pageCommunicationProtocol:1};var a;function l(e){const t=Object.assign(Object.assign({},i),e);window.postMessage(t,"*")}!function(e){e.EXTENSION_INSTALLED="query-extension-installed",e.HELPER_STATE="query-broker-state"}(a||(a={})),window.addEventListener("message",e=>{var t;e.source===window&&(e.data&&"te-eyebrow-install-page"===e.data.sender&&Object.values(a).includes(e.data.type)&&((t=e.data.type)===a.EXTENSION_INSTALLED?l({extensionInstalled:!0}):t===a.HELPER_STATE&&new Promise(e=>{chrome.runtime.sendMessage({query:o.a.helperStatusQueryName},t=>e(t.helperIsOk))}).then(e=>{l(e?n:s)})))},!1)},3:function(e){e.exports=JSON.parse('{"TE_CHROMIUM_PRODUCT":"ThousandEyes Endpoint Agent","TE_VERSION":"1.151.0","TE_BROWSER_EXT_DESCRIPTION":"Help troubleshoot performance problems with web sites you visit.","TE_CHROME_INSTALL_URL":"https://app.thousandeyes.com/install/endpoint-agent","TE_EDGE_INSTALL_URL":"https://app.thousandeyes.com/install/endpoint-agent","TE_CONTROL_API_ENDPOINT":"https://c1.eb.thousandeyes.com","TE_CONTROL_API_PRODUCT_PATH":"enterprise","TE_CHROMIUM_NATIVE_MESSAGING_HOST":"com.thousandeyes.eyebrow.entr.browserhelper","CONFIG_USER_AGENT_RPC_VERSION":"1","TE_OLD_TROUBLESHOOTER_CRX_ID":"","TE_PRODUCT":"ThousandEyes Endpoint Agent","TE_COPYRIGHT":"(c) 2014-2023 ThousandEyes. All rights reserved."}')}});