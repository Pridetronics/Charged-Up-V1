/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 307);
/******/ })
/************************************************************************/
/******/ ({

/***/ 307:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _logs__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(308);

const logs = new _logs__WEBPACK_IMPORTED_MODULE_0__["default"]();
// When adding a listener to the various runtime events, the chrome.* interfaces
// take a url filter object to notify you of events only on pages of interest
// See https://developer.chrome.com/extensions/events#filtered
// This filter is for the slides when a user is in the slideshow
const chromeApiFilterForSlideUrls = {
    url: [{ urlMatches: 'https://docs.google.com/presentation/.*/present' }]
};
// When you advance slides in a presentation, the url in the address bar updates
chrome.webNavigation.onHistoryStateUpdated.addListener((details) => {
    chrome.tabs.sendMessage(details.tabId, { slideChanged: true });
}, chromeApiFilterForSlideUrls);
// For the first slide in the presentation, there is no transition (i.e. no url update)
// so we listen for the loading being completed as well
chrome.webNavigation.onCompleted.addListener((details) => {
    chrome.tabs.sendMessage(details.tabId, { loadCompleted: true });
}, chromeApiFilterForSlideUrls);
chrome.runtime.onMessage.addListener((request, _sender, sendResponse) => {
    console.log('onMessage', request);
    if (request.action === 'add to log') {
        logs.push(request.contents);
        return false;
    }
    if (request.action === 'download logs') {
        logs.toJson().then(sendResponse);
        return true; // return true to indicate you want to send a response asynchronously
    }
    return false;
});


/***/ }),

/***/ 308:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return Logs; });
class Logs {
    constructor() {
        this.logs = [];
    }
    async push(log) {
        this.logs.push(log);
    }
    versionInfo() {
        return {
            userAgent: navigator.userAgent,
            extensionVersion: chrome.runtime.getManifest().version
        };
    }
    async toJson() {
        const logs = this.logs;
        logs.unshift(this.versionInfo());
        return JSON.stringify(logs, null, 2);
    }
}


/***/ })

/******/ });