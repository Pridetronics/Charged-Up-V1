/*!
 * 
 *     MCAFEE RESTRICTED CONFIDENTIAL
 *     Copyright (c) 2022 McAfee, LLC
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
 */(()=>{"use strict";class e{constructor(e){this.pingCommand=e,this.basePingListener()}basePingListener(e=null){chrome.runtime.onMessage.addListener(((t,s,i)=>{if(s.id!==chrome.runtime.id)return;const{command:n}=t;n===this.pingCommand&&(i({content:!0}),"function"==typeof e&&e())}))}}const t=2;class s{constructor(){this.autoplayDetectedfn=null,this.getLastSavedTimeFn=null,this.setLastSavedTimeFn=null,this.USER_PLAYED="mcafee_wa_userplayed",this.PROCESSED_VIDEO="mcafee_wa_processedvideo",this.CLICK_PLAY_INTERVAL=4e3}init(e=null,t=null,s=null){return!!e&&(this.autoplayDetectedfn=e,this.getLastSavedTimeFn=s,this.setLastSavedTimeFn=t,document.defaultView===document.defaultView.top&&window.addEventListener("click",this._setLastClickedTime.bind(this),!0),this._processLoadedElements(),!0)}_handlePlay(e){e.addEventListener("play",(()=>{this._onplay(e)}))}async _onplay(e){const t=e.src.length>0?e.src:e.currentSrc;if("true"===e.getAttribute(this.USER_PLAYED))return;let s=await this._getLastClickedTime();s=s||0;Date.now()-s<=this.CLICK_PLAY_INTERVAL?e.setAttribute(this.USER_PLAYED,"true"):this.autoplayDetectedfn(t)}_getLastClickedTime(){return this.getLastSavedTimeFn()}_setLastClickedTime(){this.setLastSavedTimeFn(Date.now())}_processVideoElement(e){"true"!==e.getAttribute(this.PROCESSED_VIDEO)&&(e.setAttribute(this.PROCESSED_VIDEO,"true"),(e.currentTime>0&&!1===e.paused&&!1===e.ended&&e.readyState>t||!0===e.autoplay)&&this._onplay(e),this._handlePlay(e))}_recursiveProcess(e){for(let t=0;t<e.childNodes.length;++t)this._recursiveProcess(e.childNodes[t]);e instanceof HTMLMediaElement&&this._processVideoElement(e)}nodeAdded(e){this._recursiveProcess(e)}_processLoadedElements(){setTimeout((()=>{const e=document.querySelectorAll("audio, video");for(let t=0;t<e.length;++t)this._processVideoElement(e[t])}),3e3)}}class i{constructor(){this.mutationHandler=null}init(e,t,i){this.mutationHandler=new s,this.mutationHandler.init(e,t,i);new MutationObserver(this.handleMutations.bind(this)).observe(document,{childList:!0,subtree:!0})}handleMutations(e){e.forEach((e=>{for(const t of e.addedNodes)this.mutationHandler.nodeAdded(t)}))}}const n="AUTO_RUN_VIDEO_SITE",a="GET_CLICK_EVENT_TIME",o="SAVE_CLICK_EVENT_TIME",r="PING_CONTENT_AUTOPLAY_DETECTION";class d{static handlePromiseMessage(e,t=null){return new Promise(((s,i)=>{chrome.runtime.sendMessage(e,(e=>{"function"==typeof t?t(s,i,e):((e,t,s)=>{chrome.runtime.lastError&&t(chrome.runtime.lastError.message),e(s)})(s,i,e)}))}))}}(new class extends e{constructor(){super(r)}autoRunVideo(e){const t=n;chrome.runtime.sendMessage({command:t,url:e})}saveLastClickedTime(e){const t=o;chrome.runtime.sendMessage({command:t,last_event:e})}getLastClickedTime(){return d.handlePromiseMessage({command:a})}main(){(new i).init((e=>this.autoRunVideo(e)),(e=>this.saveLastClickedTime(e)),(()=>this.getLastClickedTime()))}}).main()})();
//# sourceMappingURL=../sourceMap/scripts/content_autoplay_detection.js.map