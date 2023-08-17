/*!
 * 
 *     MCAFEE RESTRICTED CONFIDENTIAL
 *     Copyright (c) 2023 McAfee, LLC
 *
 *     The source code contained or described herein and all documents related
 *     to the source code ("Material") are owned by McAfee or its
 *     suppliers or licensors. Title to the Material remains with McAfee
 *     or its suppliers and licensors. The Material contains trade
 *     secrets and proprietary and confidential information of McAfee or its
 *     suppliers and licensors. The Material is protected by worldwide copyright
 *     and trade secret laws and treaty provisions. No part of the Material may
 *     be used, copied, reproduced, modified, published, uploaded, posted,
 *     transmitted, distributed, or disclosed in any way without McAfee's prior
 *     express written permission.
 *
 *     No license under any patent, copyright, trade secret or other intellectual
 *     property right is granted to or conferred upon you by disclosure or
 *     delivery of the Materials, either expressly, by implication, inducement,
 *     estoppel or otherwise. Any license under such intellectual property rights
 *     must be expressed and approved by McAfee in writing.
 *
 */(()=>{"use strict";const e="LOCAL_STORE",t="SESSION_STORE",s="CONTENT_HANDLER",n="EXECUTE_COMMAND",a="FOCUS_OR_CREATE_TAB",r="GET_BK_GLOBALS",o="GET_EXTENSION_STATUS",i="GET_TAB_DATA",c="TI_REQUEST",m="PLACEHOLDER_TEXT",l="REMOVE_TAB",d="SEND_TELEMETRY",u="SET_VIEWPORT",g="WHITELIST",E="RESET_NATIVE_SETTING",T="UPDATE_BK_NATIVE_SETTINGS",S="SHOW_SIDEBAR_MAIN",h="GET_POPUP_DATA",_="GET_SETTINGS_DATA",A="RESET_SETTINGS",N="GET_TYPOSQUATTING_DATA",O="IS_FRAME_BLOCKED",I="IS_WHITELISTED",R="ANY_BLOCKED_IFRAMES",P="ANY_BLOCKED_CRYPTOSCRIPTS",M="UNBLOCK_ALL_IFRAMES",F="VIEW_SITE_REPORT",C="SEARCH_ANNOTATION",D="UPDATE_ENGINE_STATS",p="SOCIAL_MEDIA_ANNOTATION",L="UPDATE_RAT_DETECTION_SHOWING_FLAG",f="SEARCH_SUGGEST",b="SAVE_FORM_INFO",U="GET_FORM_INFO_CACHE",y="CLEAR_FORM_INFO_CACHE",G="SAVE_MULTISTEP_LOGIN",w="GET_FD_WEIGHTS",W="GET_FD_EXCEPTIONS",k="GET_FD_CRF_PARAMS",B="CLEAR_DWS_INFO",v="GET_CACHED_DWS_INFO",H="UPDATE_DWS_WHITELIST",x="LAUNCH_IDPS_LOGIN",Y="UPDATE_DWS_SHOWN",$="GET_APS_DETAILS",K="SIGN_UP_FORM_DETECTED",q="SET_FF_POLICY_COLLECTION",V="SET_FF_POLICY_LAST_SHOWN",j="PING_CONTENT_APS_OBSERVER",Q="BROADCAST_TO_FOREGROUND";class X{constructor(e){this.pingCommand=e,this.basePingListener()}basePingListener(e=null){chrome.runtime.onMessage.addListener(((t,s,n)=>{if(s.id!==chrome.runtime.id)return;const{command:a}=t;a===this.pingCommand&&(n({content:!0}),"function"==typeof e&&e())}))}}const J={GREEN:0,YELLOW:1,RED:2,GREY:3,HACKERSAFE:4,TYPOSQUATTING:5,PHISHING:6,*[Symbol.iterator](){for(const e of Object.keys(this))"GREEN"!==e&&"GREY"!==e&&(yield this[e])}},z=2,Z=-1,ee=0,te=1,se=2,ne=3,ae=70,re=31,oe=16,ie=1,ce=chrome.runtime.getURL(""),me="ph",le="www.siteadvisor.com/",de=(new Set(["www.mcafee.com"]),chrome),ue=0,ge=0,Ee=1,Te=2,Se=3,he=4,_e=1,Ae=2,Ne=3,Oe=4,Ie={BACKGROUND:"BACKGROUND",CONTENT:"CONTENT",TELEMETRY:"TELEMETRY"},Re={DEFAULT:"color: #000000; font-weight: normal; font-style:normal; background: #FFFFFF;",BACKGROUND:"color: #8D0DBA; font-weight: bold; background: #FFFFFF;",CONTENT:"color: #F54A26; font-weight: bold; background: #FFFFFF;",TELEMETRY:"color: #147831; font-weight: bold; background: #FFFFFF;"};class Pe{static log(e,t=null){Me(e,_e,t)}static error(e,t=null){Me(e,Ae,t)}static warn(e,t=null){Me(e,Ne,t)}static debug(e,t=null){Me(e,Oe,t)}}const Me=(e,t,s)=>{const n=ue;if(n===ge)return;let a="chrome-extension:"===location.protocol?Ie.BACKGROUND:Ie.CONTENT;s&&Ie[s]&&(a=s);const r=new Date,o=t===Ae?e:`%c[${a} ${r.toLocaleString([],{hour:"2-digit",minute:"2-digit",hour12:!0})}]: %c${e}`,i=Re.DEFAULT;let c=Re[a];if(c||(c=i),n>=Te&&t===Ae&&console.error(e),n>=Ee&&t===_e&&console.log(o,c,i),n>=Se&&t===Ne){const e="color: #FFA500; font-family: sans-serif; font-weight: bolder; text-shadow: #000 1px 1px;";console.log(`%cWARN - ${o}`,e,c,i)}if(n>=he&&t===Oe){const e="color: #FF33D7; font-family: sans-serif; font-weight: bolder; text-shadow: #000 1px 1px;";console.log(`%cDEBUG - ${o}`,e,c,i)}},Fe={ANNOTATION:{},BackgroundIPC:null,BackgroundCommons:null,ContextTelemetry:null,TelemetryEventNames:null,UrlParser:null,Utils:null,Commands:{},CommonConstants:{},extension:null,SearchEngineHelper:null,prevLink:null,Logger:null};class Ce{static getResString(e,t){Fe.ANNOTATION.resourceRequestor.getResStr(e,(s=>{s?t(s):Fe.Logger.error(`Resource string ${e} failed to fetch through resource requestor`)}))}}class De{static async toGTICategoriesString(e,t=!1){const s=e=>new Promise((t=>{Ce.getResString(e,(e=>t(e)))})),n=async(e,t)=>{const n={},a=await s(`category_id_${e}_name`);if(n.name=a,t){const t=await s(`category_id_${e}_description`);n.description=t}return n},a=[];for(const s of e)a.push(n(s,t));return await Promise.all(a)}static browserTypeToString(e=!1){let t="";switch(z){case 1:t="FF";break;case 2:t="CH";break;case 3:t="ED";break;case 4:t="SF";break;default:t="UN"}return e?t.toLowerCase():t}static toAnnotationImageClass(e){let t="";switch(De.toColor(e)){case ee:t="green";break;case se:t="red";break;case te:t="yellow";break;case ne:t="grey"}return t}static isPhishingURI(e){return e&&e.includes(me)}static toBlockState(e){const t=De.toColor(e);return t!==se&&t!==te||!De.isPhishingURI(e.category)?isNaN(t)?J.GREY:t:J.PHISHING}static toColor(e){let t=Z;return void 0===e||void 0===e.trust||(t=e.trust>=ae?ee:e.trust>=re?ne:e.trust>=oe?te:e.trust>=ie?se:Z),t}static toSiteReportUrl(e){return`${de.runtime.getURL("html/site_status_site_report.html")}?url=${escape(e)}`}static isExtensionUrl(e){return e.startsWith(ce)}static isSafeUrl(e){return De.isSiteAdvisorUrl(e)||De.isExtensionUrl(e)}static isSiteAdvisorUrl(e){return e.startsWith(`http://${le}`)||e.startsWith(`https://${le}`)}static isPropertRedirectUrl(e){return De.isPropertyWebURL(e)||e.startsWith("edge://")||e.startsWith("chrome://")}static isProperWebURL(e){return!!(e.startsWith("http://")||e.startsWith("https://")||e.startsWith("ftp://"))}static getUINumber(e){if(e>=1e4)return"10k+";if(e>=1e3){let t=(e/1e3).toFixed(1);return t="0"===t.substring(2,3)?t.substring(0,1):t,`${t}k+`}return`${e}`}static hasEpochTimeElapsed(e,t){return(new Date).getTime()/1e3>e+t}static isPersonalPolicyEnabled(){return!0}static isActivityPolicyEnabled(){return!0}static blockRequest(e,t,s){de.declarativeNetRequest.getDynamicRules((n=>{0===n.filter((e=>e.condition.urlFilter===t)).length&&de.declarativeNetRequest.updateDynamicRules({addRules:[{id:n.length+1,condition:{domains:["<all_urls>"],urlFilter:t,resourceTypes:["main_frame"]},action:{type:"block"}}]},(()=>{Pe.log(`${s} ${t} ${e}`)}))}))}}class pe{static getURI(e){let t=e.split("?");return t.length>1?t[0]:(t=e.split("#"),t.length>1?t[0]:e)}static getCleanURI(e){let t=e;return t.endsWith("/")&&(t=t.slice(0,-1)),t.startsWith("http://")?t=t.slice(7):t.startsWith("https://")&&(t=t.slice(8)),this.getURI(t)}static getParam(e,t){const s=e.indexOf("?");if(-1===s)return null;const n=e.substring(s+1).split("&");for(let e=0;e<n.length;++e){const s=n[e].indexOf("=");if(-1===s)continue;if(n[e].substring(0,s)===t){return n[e].substring(s+1)}}return null}static getDomain(e){if(void 0===e||!/^https?:\/\//.test(e))return"";return new URL(e).hostname}static decodeQueryParam(e){return decodeURIComponent(e.replace(/\+/g," "))}}const Le={NONE:0,LINKEDIN:1,INSTAGRAM:2,YOUTUBE:4,FACEBOOK:8,TWITTER:16,REDDIT:32},fe=(Object.values(Le).reduce(((e,t)=>e+t)),{NONE:"NONE",ONLY_SECURE_SEARCH:"ONLY_SECURE_SEARCH",ALL:"ALL",*[Symbol.iterator](){for(const e of Object.keys(this))yield e}}),{NONE:be,...Ue}=Le,ye="SET",Ge="GET",we={name:"APS_EMAIL_LINKS",type:"array"};class We{static handlePromiseMessage(e,t=null){return new Promise(((s,n)=>{chrome.runtime.sendMessage(e,(e=>{"function"==typeof t?t(s,n,e):((e,t,s)=>{chrome.runtime.lastError&&t(chrome.runtime.lastError.message),e(s)})(s,n,e)}))}))}}class ke{static localStore(t,s){const n=e;return We.handlePromiseMessage({command:n,action:t,data:s})}static sessionStore(e,s){const n=t;return We.handlePromiseMessage({command:n,action:e,data:s})}static isFrameBlocked(e){const t=O;return We.handlePromiseMessage({command:t,url:e})}static makeMTIRequest(e,t){const s={command:c,requestData:e,referer:t};return We.handlePromiseMessage(s)}static executeCommand(e,t){const s=n;de.runtime.sendMessage({command:s,commandId:e,params:t})}static focusOrCreateTab(e){const t=a;de.runtime.sendMessage({command:t,url:e})}static closeTab(){const e=l;de.runtime.sendMessage({command:e})}static whitelist(e,t,s){const n=g;return We.handlePromiseMessage({action:e,command:n,type:t,data:s})}static getPopupData(){const e=h;return We.handlePromiseMessage({command:e})}static getSettingsData(){const e=_;return We.handlePromiseMessage({command:e})}static searchAnnotation(e,t){const s=C;return We.handlePromiseMessage({action:e,data:t,command:s})}static socialMediaAnnotation(e,t){const s=p;return We.handlePromiseMessage({action:e,data:t,command:s})}static getBkGlobals(e=!1){return new Promise((t=>{const s=r;We.handlePromiseMessage({command:s,bIncludeSearchEngines:e}).then((e=>{t(JSON.parse(e))}))}))}static viewSiteReport(e=null,t=!1){const s=F;de.runtime.sendMessage({command:s,url:e,showInNewTab:t})}static getTypoSquattingData(e){const t=N;return We.handlePromiseMessage({command:t,domain:e})}static getPlaceholderText(e){const t=m;return We.handlePromiseMessage({command:t,id:e})}static setViewPort(e,t){const s=u;de.runtime.sendMessage({command:s,x:e,y:t})}static sendTelemetry(e){const t=d;de.runtime.sendMessage({command:t,telemetry:e})}static anyBlockedIframes(e){const t=R;return We.handlePromiseMessage({command:t,frameUrls:e})}static anyBlockedCryptoScripts(){const e=P;return We.handlePromiseMessage({command:e})}static resetSettings(){const e=A;de.runtime.sendMessage({command:e})}static sendEngineStat(e){de.runtime.sendMessage({command:D,engine:e})}static contentHandler(e){const t=s;de.runtime.sendMessage({command:t,message:e})}static getTabData(e={}){const t={command:i,...e};return We.handlePromiseMessage(t)}static isWhitelisted(e){const t={command:I,url:e};return We.handlePromiseMessage(t)}static getExtensionStatus(e){return We.handlePromiseMessage({command:o,extension_id:e})}static unblockAllIframes(){const e=M;de.runtime.sendMessage({command:e})}static updateRatDetectionShowingFlag(e){const t=L;de.runtime.sendMessage({command:t,showed:e})}static getSearchSuggest(e){const t={command:f,searchTerm:e};return We.handlePromiseMessage(t)}static resetNativeSetting(e){const t=E;de.runtime.sendMessage({command:t,setting:e})}static saveFormInfo(e,t){const s=b;de.runtime.sendMessage({command:s,username:e,hostname:t})}static saveMultiStepLogin(e,t){const s=G;de.runtime.sendMessage({command:s,data:e,completeLogin:t})}static getFormInfoCache(){const e=U;return We.handlePromiseMessage({command:e})}static clearFormInfoCache(){const e=y;de.runtime.sendMessage({command:e})}static updateDWSWhitelist(e){const t=H;de.runtime.sendMessage({command:t,email:e})}static getCachedDWSInfo(e){const t={command:v,email:e};return We.handlePromiseMessage(t)}static clearCachedDWSInfo(e){const t=B;de.runtime.sendMessage({command:t,email:e})}static updateDWSShown(e){const t=Y;de.runtime.sendMessage({command:t,email:e})}static getAPSDetails(){const e=$;return We.handlePromiseMessage({command:e})}static signUpFormDetected(){const e=K;de.runtime.sendMessage({command:e})}static updateBkNativeSettings(e,t){const s=T;de.runtime.sendMessage({command:s,name:e,value:t})}static launchIDPSLogin(){const e=x;de.runtime.sendMessage({command:e})}static showSidebarMain(){const e=S;de.runtime.sendMessage({command:e})}static setFFPolicyCollection({personal:e,activity:t,permissions:s}){const n=q;de.runtime.sendMessage({command:n,personal:e,activity:t,permissions:s})}static setFFPolicyLastShown(){const e=V;de.runtime.sendMessage({command:e})}static broadcastToForeground(e){const t=Q;de.runtime.sendMessage({command:t,payload:e})}static getFDWeights(){const e=w;return We.handlePromiseMessage({command:e})}static getFDExceptions(){const e=W;return We.handlePromiseMessage({command:e})}static getFDCrfParams(){const e=k;return We.handlePromiseMessage({command:e})}}(new class extends X{constructor(){super(j)}async main(){const e=document.location.href,t=we.name,s=(await ke.sessionStore(Ge,{[t]:[]}))[t],{apsUrlList:n}=await ke.getBkGlobals(),a=n.email,r=Object.keys(a).find((t=>e.includes(t)));if(r){const e=a[r].selector;this.initEmailObserver(e,s)}else this.initEmailObserver()}initEmailObserver(e,t){const s=()=>{const s=document.querySelectorAll(e);s.length&&s.forEach((e=>{const s=e.getAttribute("href");"true"!==e.getAttribute("mcafee_aps")&&s&&De.isProperWebURL(s)&&(e.setAttribute("mcafee_aps","true"),e.addEventListener("click",(()=>{const e=pe.getDomain(s);t.push(e),ke.sessionStore(ye,{[we.name]:t})})))}))},n=new MutationObserver((()=>{s()}));s(),n.observe(document,{childList:!0,subtree:!0})}}).main()})();
//# sourceMappingURL=../sourceMap/chrome/scripts/content_aps_observer.js.map