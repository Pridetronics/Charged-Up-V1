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
/******/ 	return __webpack_require__(__webpack_require__.s = 296);
/******/ })
/************************************************************************/
/******/ ({

/***/ 296:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _reactors_presentation__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(297);
/* harmony import */ var _reactors_webapp__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(298);
// @ts-nocheck


console.log('Creating binding objects for the Poll Everywhere extension');
window.presentation = new _reactors_presentation__WEBPACK_IMPORTED_MODULE_0__["default"]();
window.webApp = new _reactors_webapp__WEBPACK_IMPORTED_MODULE_1__["default"]();
const PollEv = window.PollEv;
if (PollEv && PollEv.sync && PollEv.sync.resolve) {
    PollEv.sync.resolve();
}
window.dispatchEvent(new Event('bindingsready'));


/***/ }),

/***/ 297:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return Presentation; });
const relay = function(event) {
  return function() {
    const data = {};
    data.wrapper = {
      event,
      content: Array.prototype.slice.call(arguments)
    };

    parent.postMessage(data, "*");
  }
}

class Presentation {
  constructor() {
    this.login = this.login.bind(this);
    this.logout = this.logout.bind(this);
    this.insertSlide = this.insertSlide.bind(this);
    this.insertSlides = this.insertSlides.bind(this);
    this.insertImageSlidesByUrl = this.insertImageSlidesByUrl.bind(this);
    this.apiVersion = this.apiVersion.bind(this);
    this.openBrowser = this.openBrowser.bind(this);
  }

  login() { relay("coc:login:complete")(...arguments); }

  logout() { relay("coc:logout:complete")(...arguments); }

  insertSlide() { relay("coc:insert:slide")(...arguments); }

  insertSlides() { relay("coc:insert:slides")(...arguments); }

  insertImageSlidesByUrl() { relay("coc:insert:imageSlidesByUrl")(...arguments); }

  configApp() { relay("coc:config:app")(...arguments); }

  openBrowser() { relay("coc:open:browser")(...arguments); }

  apiVersion() { return "1.2.0"; }
}


/***/ }),

/***/ 298:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return WebApp; });
const relay = function(event) {
  return function() {
    const data = {};
    data.wrapper = {
      event,
      content: Array.prototype.slice.call(arguments)
    };

    parent.postMessage(data, "*");
  }
}

class WebApp {
  constructor() {
    this.close = this.close.bind(this)
    this.apiVersion = this.apiVersion.bind(this)
  }

  close() { relay("coc:app:close")(...arguments); }

  openBrowser() { relay("coc:open:browser")(...arguments); }

  apiVersion() { return "1.1.0"; }
}


/***/ })

/******/ });