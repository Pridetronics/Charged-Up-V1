var hypothesis = (function (exports) {
  'use strict';

  var buildType = "production";
  var manifestV3 = true;
  var key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCq7XsXE/uakq4aKMG5Smz2nc8VSaandriziGorxX08py3mTkab79GpWYu7j/hA3Yf7fkCLQnX8QoZGj7WdaMX6+b+eHxF7vYpOhEW/Bam7TOlb+5AVmL1KReG9PPTLz4dp+xA4WfK2dqFM+XN40FTbm2G/SNk3GRP3gQOxgy3ZKwIDAQAB";
  var apiUrl = "https://hypothes.is/api/";
  var authDomain = "hypothes.is";
  var bouncerUrl = "https://hyp.is/";
  var serviceUrl = "https://hypothes.is/";
  var oauthClientId = "fd23fe2e-7792-11e7-8e16-23e47a1799d4";
  var sentryPublicDSN = "https://934d4f62912b47d8bb03c28ae6670cf8@app.getsentry.com/69811";
  var browserIsChrome = true;
  var appType = "chrome-extension";
  var version = "1.1299.0.0";
  var versionName = "Official Build";
  var raven = {
  	dsn: "https://934d4f62912b47d8bb03c28ae6670cf8@app.getsentry.com/69811",
  	release: "1.1299.0.0"
  };
  var rawSettings = {
  	buildType: buildType,
  	manifestV3: manifestV3,
  	key: key,
  	apiUrl: apiUrl,
  	authDomain: authDomain,
  	bouncerUrl: bouncerUrl,
  	serviceUrl: serviceUrl,
  	oauthClientId: oauthClientId,
  	sentryPublicDSN: sentryPublicDSN,
  	browserIsChrome: browserIsChrome,
  	appType: appType,
  	version: version,
  	versionName: versionName,
  	raven: raven
  };

  /**
   * Incomplete type for settings in the `settings.json` file.
   *
   * This contains only the settings that the background script uses. Other
   * settings are used when generating the `manifest.json` file.
   */


  /**
   * Configuration data for the extension.
   */
  const settings = {
    ...rawSettings,
    // Ensure API url does not end with '/'
    apiUrl: rawSettings.apiUrl.replace(/\/$/, '')
  };

  /**
   * This module provides a wrapper around the Chrome / WebExtension APIs that
   * the extension uses.
   *
   * It has several purposes:
   *  - Provide a Promise-returning interface for all async APIs
   *  - Abstract over the differences between different browsers
   *  - Provide a seam that can be easily mocked in tests
   */

  /**
   * Wrap the browser APIs exposed via the `chrome` object to return promises.
   *
   * This is exposed for testing. Consumers should use {@link chromeAPI}.
   */
  function getChromeAPI(chrome = globalThis.chrome) {
    var _chrome$scripting;
    // In the test environment, the `chrome` global may not exist.
    if (typeof chrome === 'undefined') {
      // The `as never` causes this branch to be ignored when TS determines the
      // return type of this function.
      return null;
    }

    /**
     * Cache of Promise-ified APIs. This is used so that APIs which are looked up
     * on-demand, eg. because they are optional and may not exist when `getChromeAPI`
     * is first called, are only created once.
     */
    const cache = new Map();

    /**
     * Convert an async callback-accepting Chrome API to a Promise-returning version.
     *
     * TypeScript may complain if the API has a Manifest V3-only overload that
     * returns a Promise. Use {@link promisifyAlt} as a workaround.
     *
     * This wrapper can be removed once the extension becomes Manifest V3-only.
     *
     * @param fn - The original Chrome API that accepts a callback.
     * @return Wrapped API that doesn't take a callback but returns a Promise instead
     */
    function promisify(fn) {
      const cached = cache.get(fn);
      if (cached) {
        return cached;
      }
      return (...args) => {
        return new Promise((resolve, reject) => {
          fn(...args, result => {
            const lastError = chrome.runtime.lastError;
            if (lastError) {
              reject(lastError);
            } else {
              resolve(result);
            }
          });
        });
      };
    }
    function promisifyAlt(fn) {
      // @ts-expect-error
      return promisify(fn);
    }
    const browserAction = chrome.browserAction ?? chrome.action;
    return {
      browserAction: {
        onClicked: browserAction.onClicked,
        setBadgeBackgroundColor: promisify(browserAction.setBadgeBackgroundColor),
        setBadgeText: promisify(browserAction.setBadgeText),
        setIcon: promisify(browserAction.setIcon),
        setTitle: promisify(browserAction.setTitle)
      },
      extension: {
        isAllowedFileSchemeAccess: promisify(chrome.extension.isAllowedFileSchemeAccess)
      },
      management: {
        getSelf: promisify(chrome.management.getSelf)
      },
      runtime: {
        id: chrome.runtime.id,
        getURL: chrome.runtime.getURL,
        onMessage: chrome.runtime.onMessage,
        onMessageExternal: chrome.runtime.onMessageExternal,
        onInstalled: chrome.runtime.onInstalled,
        onUpdateAvailable: chrome.runtime.onUpdateAvailable,
        reload: chrome.runtime.reload,
        // Firefox (as of v92) does not support `requestUpdateCheck`.
        requestUpdateCheck: chrome.runtime.requestUpdateCheck ? promisify(chrome.runtime.requestUpdateCheck) : null
      },
      permissions: {
        getAll: promisify(chrome.permissions.getAll),
        request: promisify(chrome.permissions.request)
      },
      tabs: {
        create: promisify(chrome.tabs.create),
        get: promisifyAlt(chrome.tabs.get),
        onCreated: chrome.tabs.onCreated,
        onReplaced: chrome.tabs.onReplaced,
        onRemoved: chrome.tabs.onRemoved,
        onUpdated: chrome.tabs.onUpdated,
        query: promisifyAlt(chrome.tabs.query),
        update: promisify(chrome.tabs.update),
        // Manifest V2 only.
        executeScript: promisifyAlt(chrome.tabs.executeScript)
      },
      // Manifest V3 only.
      scripting: {
        executeScript: (_chrome$scripting = chrome.scripting) === null || _chrome$scripting === void 0 ? void 0 : _chrome$scripting.executeScript
      },
      storage: {
        // Methods of storage areas (sync, local, managed) need to be bound.
        // Standalone functions in Chrome API namespaces do not.
        sync: {
          get: promisify(chrome.storage.sync.get.bind(chrome.storage.sync))
        }
      },
      // APIs that require optional permissions.
      //
      // These are resolved on-demand because the `chrome.<namespace>` properties
      // will not exist unless the extension has the required permissions.
      //
      // If a permission is revoked, the property remains accessible until the
      // page/worker is reloaded, but calling APIs may fail.

      get webNavigation() {
        return {
          getAllFrames: promisifyAlt(chrome.webNavigation.getAllFrames)
        };
      }
    };
  }

  /**
   * Entry point for browser APIs.
   *
   * This has the same shape as the `chrome` or `browser` object.
   */
  const chromeAPI = getChromeAPI();

  // The functions below are wrappers around the extension APIs for scripting
  // which abstract over differences between browsers (eg. Manifest V2 vs Manifest V3)
  // and provide a simpler and more strongly typed interface.

  /**
   * Generate a string of code which can be eval-ed to produce the same result
   * as invoking `func` with `args`.
   */
  function codeStringForFunctionCall(func, args) {
    return `(${func})(${args.map(arg => JSON.stringify(arg)).join(',')})`;
  }
  /**
   * Execute a JavaScript file within a tab.
   */
  async function executeScript({
    tabId,
    frameId,
    file
  }, chromeAPI_ = chromeAPI) {
    if (settings.manifestV3) {
      const target = {
        tabId
      };
      if (frameId) {
        target.frameIds = [frameId];
      }
      const results = await chromeAPI_.scripting.executeScript({
        target,
        files: [file]
      });
      return results[0].result;
    }
    const result = await chromeAPI_.tabs.executeScript(tabId, {
      frameId,
      file
    });
    return result[0];
  }
  /**
   * Execute a JavaScript function within a tab.
   */
  async function executeFunction({
    tabId,
    frameId,
    func,
    args
  }, chromeAPI_ = chromeAPI) {
    if (settings.manifestV3) {
      const target = {
        tabId
      };
      if (frameId) {
        target.frameIds = [frameId];
      }
      const results = await chromeAPI_.scripting.executeScript({
        target,
        func,
        args
      });
      return results[0].result;
    }
    const code = codeStringForFunctionCall(func, args);
    const result = await chromeAPI_.tabs.executeScript(tabId, {
      frameId,
      code
    });
    return result[0];
  }
  function getExtensionId(chromeAPI_ = chromeAPI) {
    return chromeAPI_.runtime.id;
  }

  function _defineProperty(obj, key, value) {
    key = _toPropertyKey(key);
    if (key in obj) {
      Object.defineProperty(obj, key, {
        value: value,
        enumerable: true,
        configurable: true,
        writable: true
      });
    } else {
      obj[key] = value;
    }
    return obj;
  }
  function _toPrimitive(input, hint) {
    if (typeof input !== "object" || input === null) return input;
    var prim = input[Symbol.toPrimitive];
    if (prim !== undefined) {
      var res = prim.call(input, hint || "default");
      if (typeof res !== "object") return res;
      throw new TypeError("@@toPrimitive must return a primitive value.");
    }
    return (hint === "string" ? String : Number)(input);
  }
  function _toPropertyKey(arg) {
    var key = _toPrimitive(arg, "string");
    return typeof key === "symbol" ? key : String(key);
  }

  // Each button state has two icons one for normal resolution (19) and one
  // for hi-res screens (38).
  const icons = {
    active: {
      19: 'images/browser-icon-active.png',
      38: 'images/browser-icon-active@2x.png'
    },
    inactive: {
      19: 'images/browser-icon-inactive.png',
      38: 'images/browser-icon-inactive@2x.png'
    }
  };

  /**
   * Themes to apply to the toolbar icon badge depending on the type of
   * build. Production builds use the default color and no text
   */
  const badgeThemes = {
    dev: {
      defaultText: 'DEV',
      color: '#5BCF59' // Emerald green
    },

    qa: {
      defaultText: 'QA',
      color: '#EDA061' // Porche orange-pink
    }
  };

  /**
   * Controls the display of the browser action button setting the icon, title
   * and badges depending on the current state of the tab.
   *
   * BrowserAction is responsible for mapping the logical H state of
   * a tab (whether the extension is active, annotation count) to
   * the badge state.
   */
  class BrowserAction {
    /**
     * Updates the state of the browser action to reflect the logical
     * H state of a tab.
     */
    update(tabId, state) {
      let activeIcon;
      let title;
      let badgeText = '';
      switch (state.state) {
        case 'active':
          activeIcon = icons.active;
          title = 'Hypothesis is active';
          break;
        case 'inactive':
          activeIcon = icons.inactive;
          title = 'Hypothesis is inactive';
          break;
        case 'errored':
          activeIcon = icons.inactive;
          title = 'Hypothesis failed to load';
          badgeText = '!';
          break;
      }

      // display the annotation count on the badge
      if (state.state !== 'errored' && state.annotationCount) {
        let totalString = state.annotationCount.toString();
        if (state.annotationCount > 999) {
          totalString = '999+';
        }
        let countLabel;
        if (state.annotationCount === 1) {
          countLabel = "There's 1 annotation on this page";
        } else {
          countLabel = `There are ${totalString} annotations on this page`;
        }
        title = countLabel;
        badgeText = totalString;
      }

      // update the badge style to reflect the build type
      const badgeTheme = badgeThemes[settings.buildType];
      if (badgeTheme) {
        chromeAPI.browserAction.setBadgeBackgroundColor({
          tabId,
          color: badgeTheme.color
        });
        if (!badgeText) {
          badgeText = badgeTheme.defaultText;
        }
      }
      chromeAPI.browserAction.setBadgeText({
        tabId,
        text: badgeText
      });
      chromeAPI.browserAction.setIcon({
        tabId,
        path: activeIcon
      });
      chromeAPI.browserAction.setTitle({
        tabId,
        title
      });
    }
  }
  _defineProperty(BrowserAction, "icons", icons);

  /**
   * Subset of the client configuration which causes the client to show a
   * particular set of annotations automatically after it loads.
   *
   * See https://h-client.readthedocs.io/en/latest/publishers/config/#config-settings
   */

  /**
   * Extracts the direct-linking query from the URL if any.
   *
   * If present, the query causes the extension to activate automatically and
   * show the matching set of annotations.
   *
   * @param url -
   *   The URL which may contain a '#annotations:' fragment specifying which
   *   annotations to show.
   * @return - The direct link query translated into client configuration settings.
   */
  function directLinkQuery(url) {
    // Annotation IDs are url-safe-base64 identifiers
    // See https://tools.ietf.org/html/rfc4648#page-7
    const idMatch = url.match(/#annotations:([A-Za-z0-9_-]+)$/);
    if (idMatch) {
      return {
        annotations: idMatch[1]
      };
    }
    const queryMatch = url.match(/#annotations:query:(.*)$/);
    if (queryMatch) {
      const query = decodeURIComponent(queryMatch[1]);
      return {
        query
      };
    }

    // Group IDs (and other "pubids" in h) are a subset of ASCII letters and
    // digits. As a special exception, the "Public" group has underscores in its
    // ID ("__world__").
    const groupMatch = url.match(/#annotations:group:([A-Za-z0-9_]+)$/);
    if (groupMatch) {
      return {
        group: groupMatch[1]
      };
    }
    return null;
  }

  class ExtensionError extends Error {}
  class LocalFileError extends ExtensionError {}
  class NoFileAccessError extends ExtensionError {}
  class RestrictedProtocolError extends ExtensionError {}
  class BlockedSiteError extends ExtensionError {}
  class AlreadyInjectedError extends ExtensionError {}
  class RequestCanceledError extends Error {}
  class BadgeUriError extends Error {}

  /**
   * Returns true if `err` is a recognized 'expected' error.
   */
  function isKnownError(err) {
    return err instanceof ExtensionError;
  }
  const IGNORED_ERRORS = [
  // Errors that can happen when the tab is closed during injection
  /The tab was closed/, /No tab with id.*/,
  // Attempts to access pages for which Chrome does not allow scripting
  /Cannot access contents of.*/, /The extensions gallery cannot be scripted/,
  // The extension is disabled on LMS assignments to avoid confusion with the
  // embedded Hypothesis instance. The user can still use the extension on other
  // pages hosted in the LMS itself.
  /Hypothesis extension can't be used on Hypothesis LMS assignments/];

  /**
   * Returns true if a given `err` is anticipated during sidebar injection, such
   * as the tab being closed by the user, and should not be reported to Sentry.
   *
   * @param err - The Error-like object
   */
  function shouldIgnoreInjectionError(err) {
    if (IGNORED_ERRORS.some(pattern => err.message.match(pattern))) {
      return true;
    }
    if (isKnownError(err)) {
      return true;
    }
    return false;
  }

  /**
   * Report an error.
   *
   * All errors are logged to the console. Additionally unexpected errors,
   * ie. those which are not instances of ExtensionError, are reported to
   * Sentry.
   *
   * @param error - The error which happened.
   * @param when - Describes the context in which the error occurred.
   * @param context - Additional context for the error.
   */
  function report(error, when, context) {
    console.error(when, error, context);
  }

  /**
   * A controller for displaying help pages. These are bound to extension
   * specific errors (found in errors.js) but can also be triggered manually.
   */
  class HelpPage {
    /**
     * Accepts an instance of errors.ExtensionError and displays an appropriate
     * help page if one exists.
     *
     * @param tab - The tab where the error occurred
     * @param error - The error to display, usually an instance of {@link ExtensionError}
     */
    showHelpForError(tab, error) {
      let section;
      if (error instanceof LocalFileError) {
        section = 'local-file';
      } else if (error instanceof NoFileAccessError) {
        section = 'no-file-access';
      } else if (error instanceof RestrictedProtocolError) {
        section = 'restricted-protocol';
      } else if (error instanceof BlockedSiteError) {
        section = 'blocked-site';
      } else {
        section = 'other-error';
      }
      const url = new URL(chromeAPI.runtime.getURL('/help/index.html'));
      if (error) {
        url.searchParams.append('message', error.message);
      }
      url.hash = section;
      chromeAPI.tabs.create({
        index: tab.index + 1,
        url: url.toString(),
        openerTabId: tab.id
      });
    }
  }

  /** Details of the detected content type. */

  /**
   * Detect the type of content in the current document.
   *
   * This function is injected as a content script into tabs in order to detect
   * the type of content on the page (PDF, HTML) etc.  by sniffing for viewer
   * plugins.
   *
   * In future this could also be extended to support extraction of the URLs of
   * content in embedded viewers where that differs from the tab's main URL.
   *
   * @param document_ - Document to query
   */
  /* istanbul ignore next */
  function detectContentType(document_ = document) {
    function detectChromePDFViewer() {
      // When viewing a PDF in Chrome, the viewer consists of a top-level
      // document with an <embed> tag, which in turn instantiates an inner HTML
      // document providing the PDF viewer UI plus another <embed> tag which
      // instantiates the native PDF renderer.
      //
      // The selector below matches the <embed> tag in the top-level document. To
      // see this document, open the developer tools from Chrome's menu rather
      // than right-clicking on the viewport and selecting the 'Inspect' option
      // which will instead show the _inner_ document.
      if (document_.querySelector('embed[type="application/pdf"]')) {
        return {
          type: 'PDF'
        };
      }
      return null;
    }
    function detectFirefoxPDFViewer() {
      // The Firefox PDF viewer is an instance of PDF.js.
      //
      // The Firefox PDF plugin specifically can be detected via the <base>
      // tag it includes, which can be done from a content script (which runs
      // in an isolated JS world from the page's own scripts).
      //
      // Generic PDF.js detection can be done by looking for the
      // `window.PDFViewerApplication` object. This however requires running JS
      // code in the same JS context as the page's own code.
      if (document_.baseURI.indexOf('resource://pdf.js') === 0) {
        return {
          type: 'PDF'
        };
      }
      return null;
    }
    const detectFns = [detectChromePDFViewer, detectFirefoxPDFViewer];
    for (let i = 0; i < detectFns.length; i++) {
      const typeInfo = detectFns[i]();
      if (typeInfo) {
        return typeInfo;
      }
    }
    return {
      type: 'HTML'
    };
  }

  const CONTENT_TYPE_HTML = 'HTML';
  const CONTENT_TYPE_PDF = 'PDF';
  const CONTENT_TYPE_VITALSOURCE = 'VITALSOURCE';
  const CONTENT_TYPE_LMS = 'LMS';

  /* istanbul ignore next - Code coverage breaks `eval`-ing of this function in tests. */
  function setClientConfig(config, extensionId) {
    const script = document.createElement('script');
    script.className = 'js-hypothesis-config';
    script.type = 'application/json';
    script.textContent = JSON.stringify(config);
    script.setAttribute('data-extension-id', extensionId);
    // This ensures the client removes the script when the extension is deactivated
    script.setAttribute('data-remove-on-unload', '');
    document.head.appendChild(script);
  }

  /**
   * Function that is run in a frame to test whether the Hypothesis client is
   * active there.
   *
   * @param extensionURL - Root URL for the extension, of the form
   *   "chrome-extension://{ID}/".
   */
  function isClientActive(extensionURL) {
    const annotatorLink = document.querySelector('link[type="application/annotator+html"]');
    return (annotatorLink === null || annotatorLink === void 0 ? void 0 : annotatorLink.href.startsWith(extensionURL)) ?? false;
  }

  /**
   * A Chrome tab for which we have ID and URL information.
   *
   * This type avoids the need to check everywhere we access these properties.
   */

  /**
   * Check that a tab has the necessary metadata to inject or un-inject the client.
   *
   * All "normal" tabs should have this information because of the extension's
   * permissions.
   */
  function checkTab(tab) {
    if (!tab.id || !tab.url) {
      throw new Error('Tab is missing ID or URL');
    }
    return tab;
  }

  /**
   * The SidebarInjector is used to deploy and remove the Hypothesis sidebar
   * from tabs. It also deals with loading PDF documents into the PDF.js viewer
   * when applicable.
   */
  class SidebarInjector {
    constructor() {
      _defineProperty(this, "isClientActiveInTab", void 0);
      _defineProperty(this, "injectIntoTab", void 0);
      _defineProperty(this, "removeFromTab", void 0);
      _defineProperty(this, "requestExtraPermissionsForTab", void 0);
      const pdfViewerBaseURL = chromeAPI.runtime.getURL('/pdfjs/web/viewer.html');

      /**
       * Check for the presence of the client in a browser tab.
       *
       * If code cannot be run in this tab to check the state of the client, it is
       * assumed to not be active.
       */
      this.isClientActiveInTab = async tab => {
        const tab_ = checkTab(tab);

        // If this is our PDF viewer, the client is definitely active.
        if (isPDFViewerURL(tab_.url)) {
          return true;
        }

        // In the VitalSource book reader, we need to test a specific frame.
        let frameId;
        if (isVitalSourceURL(tab_.url)) {
          const vsFrame = await getVitalSourceViewerFrame(tab_);
          if (vsFrame) {
            frameId = vsFrame.frameId;
          }
        }
        try {
          const extensionURL = chromeAPI.runtime.getURL('/');
          const isActive = await executeFunction({
            tabId: tab_.id,
            frameId,
            func: isClientActive,
            args: [extensionURL]
          });
          return isActive;
        } catch {
          // We failed to run code in this tab, eg. because it is a URL that
          // disallows extension scripting or it is being unloaded.
          return false;
        }
      };

      /**
       * Injects the Hypothesis sidebar into the tab provided.
       *
       * Certain URLs (eg. VitalSource books) may require extra permissions to
       * inject. These must be obtained by calling {@link requestExtraPermissionsForTab},
       * directly after the user clicks the extension's toolbar icon, before calling
       * this method.
       *
       * @param tab - A tab object representing the tab to insert the sidebar into.
       * @param config - An object containing configuration info that is passed to
       *   the app when it loads.
       *
       * Returns a promise that will be resolved if the injection succeeded
       * otherwise it will be rejected with an error.
       */
      this.injectIntoTab = (tab, config = {}) => {
        const tab_ = checkTab(tab);
        if (isFileURL(tab_.url)) {
          return injectIntoLocalDocument(tab_, config);
        } else {
          return injectIntoRemoteDocument(tab_, config);
        }
      };

      /**
       * Removes the Hypothesis sidebar from the tab provided.
       *
       * Returns a promise that will be resolved if the removal succeeded
       * otherwise it will be rejected with an error.
       */
      this.removeFromTab = tab => {
        const tab_ = checkTab(tab);
        if (isPDFViewerURL(tab_.url)) {
          return removeFromPDF(tab_);
        } else if (isVitalSourceURL(tab_.url)) {
          return removeFromVitalSource(tab_);
        } else {
          return removeFromHTML(tab_);
        }
      };

      /**
       * Request additional permissions that are required to inject Hypothesis
       * into a given tab.
       *
       * Ideally the permissions request would just be part of {@link injectIntoTab}
       * however it needs to be performed immediately after the user clicks the
       * extension's toolbar icon, before any async calls, otherwise it will fail
       * due to lack of a user gesture. See https://bugs.chromium.org/p/chromium/issues/detail?id=1363490.
       */
      this.requestExtraPermissionsForTab = async tab => {
        const tab_ = checkTab(tab);
        if (isVitalSourceURL(tab_.url)) {
          return await chromeAPI.permissions.request({
            permissions: ['webNavigation']
          });
        } else {
          // No extra permissions needed for other tabs.
          return true;
        }
      };
      function getPDFViewerURL(url) {
        // Encode the original URL but preserve the fragment. Preserving the
        // fragment was originally done to support `#annotations:` fragments that
        // bouncer used to use. Bouncer and the extension now use a different
        // mechanism to pass direct-linked annotation IDs to the client. However
        // preserving the fragment may be useful for other reasons.
        const parsedURL = new URL(url);
        const hash = parsedURL.hash;
        parsedURL.hash = '';
        const encodedURL = encodeURIComponent(parsedURL.href);
        return `${pdfViewerBaseURL}?file=${encodedURL}${hash}`;
      }

      /**
       * Returns true if the extension is permitted to inject a content script into
       * a tab with a given URL.
       */
      async function canInjectScript(url) {
        if (isFileURL(url)) {
          return chromeAPI.extension.isAllowedFileSchemeAccess();
        }
        return isSupportedURL(url);
      }

      /**
       * Guess the content type of a page from the URL alone.
       *
       * This is a fallback for when it is not possible to inject
       * a content script to determine the type of content in the page.
       */
      function guessContentTypeFromURL(url) {
        if (url.includes('.pdf')) {
          return CONTENT_TYPE_PDF;
        } else {
          return CONTENT_TYPE_HTML;
        }
      }
      function isVitalSourceURL(url) {
        return url.startsWith('https://bookshelf.vitalsource.com/');
      }
      function isLMSAssignmentURL(url) {
        const {
          origin
        } = new URL(url);
        // Matches origins like `lms.hypothes.is`, `qa-lms.hypothes.is`, `lms.ca.hypothes.is`.
        return /\blms\b/.test(origin) && origin.endsWith('.hypothes.is');
      }
      async function detectTabContentType(tab) {
        if (isPDFViewerURL(tab.url)) {
          return CONTENT_TYPE_PDF;
        }
        if (isVitalSourceURL(tab.url)) {
          return CONTENT_TYPE_VITALSOURCE;
        }
        if (isLMSAssignmentURL(tab.url)) {
          return CONTENT_TYPE_LMS;
        }
        const canInject = await canInjectScript(tab.url);
        if (canInject) {
          const result = await executeFunction({
            tabId: tab.id,
            func: detectContentType,
            args: []
          });
          if (result) {
            return result.type;
          } else {
            // If the content script threw an exception,
            // frameResults may be null or undefined.
            //
            // In that case, fall back to guessing based on the
            // tab URL
            return guessContentTypeFromURL(tab.url);
          }
        } else {
          // We cannot inject a content script in order to determine the
          // file type, so fall back to a URL-based mechanism
          return guessContentTypeFromURL(tab.url);
        }
      }

      /**
       * Returns true if a tab is displaying a PDF using the PDF.js-based
       * viewer bundled with the extension.
       */
      function isPDFViewerURL(url) {
        return url.startsWith(pdfViewerBaseURL);
      }
      function isFileURL(url) {
        return url.startsWith('file:');
      }
      function isSupportedURL(url) {
        // Injection of content scripts is limited to a small number of protocols,
        // see https://developer.chrome.com/extensions/match_patterns
        const parsedURL = new URL(url);
        return ['http:', 'https:', 'ftp:'].includes(parsedURL.protocol);
      }
      async function injectIntoLocalDocument(tab, config) {
        const type = await detectTabContentType(tab);
        if (type === CONTENT_TYPE_PDF) {
          return injectIntoLocalPDF(tab, config);
        } else {
          throw new LocalFileError('Local non-PDF files are not supported');
        }
      }
      async function injectIntoRemoteDocument(tab, config) {
        if (isPDFViewerURL(tab.url)) {
          return;
        }
        if (!isSupportedURL(tab.url)) {
          // Chrome does not permit extensions to inject content scripts
          // into (chrome*):// URLs and other custom schemes.
          //
          // A common case where this happens is when the user has an
          // extension installed that provides a custom viewer for PDFs
          // (or some other format). In some cases we could extract the original
          // URL and open that in the Hypothesis viewer instead.
          const protocol = tab.url.split(':')[0];
          throw new RestrictedProtocolError(`Cannot load Hypothesis into ${protocol} pages`);
        }
        const type = await detectTabContentType(tab);
        if (type === CONTENT_TYPE_PDF) {
          await injectIntoPDF(tab, config);
        } else if (type === CONTENT_TYPE_VITALSOURCE) {
          await injectIntoVitalSourceReader(tab, config);
        } else if (type === CONTENT_TYPE_LMS) {
          // The extension is blocked on LMS assignments to avoid confusion with the
          // embedded Hypothesis instance. The user can still use the extension on other
          // pages hosted in the LMS itself.
          throw new BlockedSiteError("Hypothesis extension can't be used on Hypothesis LMS assignments");
        } else {
          // FIXME - Nothing actually sets `installedURL`. It used to be part of
          // the client's boot script. See e0bf3fd2a09414170eb991d7837bf6acd821502b.
          const result = await injectIntoHTML(tab, config);
          if (typeof (result === null || result === void 0 ? void 0 : result.installedURL) === 'string' && !result.installedURL.includes(chromeAPI.runtime.getURL('/'))) {
            throw new AlreadyInjectedError('Hypothesis is already injected into this page');
          }
        }
      }
      function injectIntoPDF(tab, config) {
        if (isPDFViewerURL(tab.url)) {
          return Promise.resolve();
        }
        const onMessage = chromeAPI.runtime.onMessage;
        const listener = (request, sender, sendResponse) => {
          var _sender$tab;
          if (((_sender$tab = sender.tab) === null || _sender$tab === void 0 ? void 0 : _sender$tab.id) === tab.id && (request === null || request === void 0 ? void 0 : request.type) === 'getConfigForTab') {
            sendResponse(config);
            onMessage.removeListener(listener);
          }
        };
        onMessage.addListener(listener);
        return chromeAPI.tabs.update(tab.id, {
          url: getPDFViewerURL(tab.url)
        });
      }
      async function injectIntoLocalPDF(tab, config) {
        const isAllowed = await chromeAPI.extension.isAllowedFileSchemeAccess();
        if (isAllowed) {
          await injectIntoPDF(tab, config);
        } else {
          throw new NoFileAccessError('Local file scheme access denied');
        }
      }
      async function injectIntoHTML(tab, config) {
        await injectConfig(tab.id, config);
        return executeClientBootScript(tab.id);
      }
      async function removeFromPDF(tab) {
        const parsedURL = new URL(tab.url);
        const originalURL = parsedURL.searchParams.get('file');
        if (!originalURL) {
          throw new Error(`Failed to extract original URL from ${tab.url}`);
        }
        let hash = parsedURL.hash;

        // If the original URL was a direct link, drop the #annotations fragment
        // as otherwise the Chrome extension will re-activate itself on this tab
        // when the original URL loads.
        if (hash.startsWith('#annotations:')) {
          hash = '';
        }
        await chromeAPI.tabs.update(tab.id, {
          url: decodeURIComponent(originalURL) + hash
        });
      }
      async function removeFromHTML(tab) {
        if (!isSupportedURL(tab.url)) {
          return;
        }
        await executeScript({
          tabId: tab.id,
          file: '/unload-client.js'
        });
      }

      /**
       * Find the frame within the VitalSource Bookshelf reader into which the
       * Hypothesis client should be loaded.
       *
       * The frame hierarchy will look like:
       *
       * bookshelf.vitalsource.com (Main frame)
       * |- jigsaw.vitalsource.com (Ebook reader. This is where we want to inject the client)
       *     |- jigsaw.vitalsource.com (Content of current chapter)
       */
      async function getVitalSourceViewerFrame(tab) {
        var _await$chromeAPI$perm;
        // Using `chrome.webNavigation.getAllFrames` requires asking for the
        // `webNavigation` permission which results in a scary prompt about reading
        // browser history, even though we only want to get frames for the current
        // tab :(
        //
        // The request for permissions must happen immediately after clicking
        // the browser action, to avoid an error about it happening outside a user
        // gesture [1]. This is done by calling `requestExtraPermissionsForTab`
        // before `injectIntoTab`.
        //
        // [1] https://bugs.chromium.org/p/chromium/issues/detail?id=1363490
        const canUseWebNavigation = (_await$chromeAPI$perm = (await chromeAPI.permissions.getAll()).permissions) === null || _await$chromeAPI$perm === void 0 ? void 0 : _await$chromeAPI$perm.includes('webNavigation');
        if (!canUseWebNavigation) {
          throw new Error('The extension was not granted required permissions');
        }
        const frames = await chromeAPI.webNavigation.getAllFrames({
          tabId: tab.id
        });
        if (!frames) {
          throw new Error('Could not list frames in tab');
        }
        return frames.find(frame => {
          const frameURL = new URL(frame.url);
          if (frameURL.hostname !== 'jigsaw.vitalsource.com' || !frameURL.pathname.startsWith('/mosaic/wrapper.html')) {
            return null;
          }
          return frame;
        });
      }
      async function injectIntoVitalSourceReader(tab, config) {
        const frame = await getVitalSourceViewerFrame(tab);
        if (!frame) {
          throw new Error('Book viewer frame not found');
        }
        await injectConfig(tab.id, config, frame.frameId);
        await executeClientBootScript(tab.id, frame.frameId);
      }
      async function removeFromVitalSource(tab) {
        const frame = await getVitalSourceViewerFrame(tab);
        if (!frame) {
          return;
        }
        await executeScript({
          tabId: tab.id,
          frameId: frame.frameId,
          file: '/unload-client.js'
        });
      }

      /**
       * Inject configuration for the Hypothesis client into the page via a
       * JSON <script> tag.
       */
      function injectConfig(tabId, clientConfig, frameId) {
        const extensionId = getExtensionId();
        return executeFunction({
          tabId,
          frameId,
          func: setClientConfig,
          args: [clientConfig, extensionId]
        });
      }
      async function executeClientBootScript(tabId, frameId) {
        return executeScript({
          tabId,
          frameId,
          file: '/client/build/boot.js'
        });
      }
    }
  }

  function getDefaultExportFromCjs (x) {
  	return x && x.__esModule && Object.prototype.hasOwnProperty.call(x, 'default') ? x['default'] : x;
  }

  /*!
   * is-primitive <https://github.com/jonschlinkert/is-primitive>
   *
   * Copyright (c) 2014-2015, Jon Schlinkert.
   * Licensed under the MIT License.
   */

  // see http://jsperf.com/testing-value-is-primitive/7
  var isPrimitive$1 = function isPrimitive(value) {
    return value == null || (typeof value !== 'function' && typeof value !== 'object');
  };

  /*!
   * is-equal-shallow <https://github.com/jonschlinkert/is-equal-shallow>
   *
   * Copyright (c) 2015, Jon Schlinkert.
   * Licensed under the MIT License.
   */

  var isPrimitive = isPrimitive$1;

  var isEqualShallow = function isEqual(a, b) {
    if (!a && !b) { return true; }
    if (!a && b || a && !b) { return false; }

    var numKeysA = 0, numKeysB = 0, key;
    for (key in b) {
      numKeysB++;
      if (!isPrimitive(b[key]) || !a.hasOwnProperty(key) || (a[key] !== b[key])) {
        return false;
      }
    }
    for (key in a) {
      numKeysA++;
    }
    return numKeysA === numKeysB;
  };

  var isShallowEqual = /*@__PURE__*/getDefaultExportFromCjs(isEqualShallow);

  const ALLOWED_PROTOCOLS = new Set(['http:', 'https:']);

  // The following sites are personal in nature, high potential traffic
  // and URLs don't correspond to identifiable content
  const BLOCKED_HOSTNAMES = new Set(['facebook.com', 'www.facebook.com', 'mail.google.com']);

  /**
   * Encodes a string for use in a query parameter.
   */
  function encodeUriQuery(val) {
    return encodeURIComponent(val).replace(/%20/g, '+');
  }

  /**
   * Returns a normalized version of URI for use in badge requests, or throws BadgeUrlError
   * if badge requests cannot be made for the given URL
   *
   * The normalization consist on (1) adding a final '/' at the end of the URL and
   * (2) removing the fragment from the URL. The URL fragment can be ignored as it
   *  will result the same badge count.
   *
   *  In addition, this normalization facilitates the identification of unique URLs.
   *
   * @return URL without fragment
   * @throws Will throw if URL is invalid or should not be sent to the 'badge' request endpoint
   */
  function uriForBadgeRequest(uri) {
    const url = new URL(uri);
    if (!ALLOWED_PROTOCOLS.has(url.protocol)) {
      throw new BadgeUriError('Blocked protocol');
    }
    if (BLOCKED_HOSTNAMES.has(url.hostname)) {
      throw new BadgeUriError('Blocked hostname');
    }
    url.hash = '';
    return url.toString();
  }

  /**
   * Queries the Hypothesis service that provides statistics about the annotations
   * for a given URL.
   *
   * @throws Will throw a variety of errors: network, json parsing, or wrong format errors.
   */
  async function fetchAnnotationCount(uri) {
    const response = await fetch(settings.apiUrl + '/badge?uri=' + encodeUriQuery(uri), {
      credentials: 'include'
    });
    const data = await response.json();
    if (data && typeof data.total === 'number') {
      return data.total;
    }
    throw new Error('Unable to parse badge response');
  }

  /**
   * Hypothesis-related state for a specific tab.
   *
   * This should really be named `TabState` but that will conflict with the
   * `TabState` class below. That class needs to be renamed first.
   */

  /**
   * The default H state for a new browser tab.
   */
  const DEFAULT_STATE = {
    state: 'inactive',
    annotationCount: 0,
    extensionSidebarInstalled: false,
    ready: false,
    error: undefined
  };

  /**
   * Represents a pending request to the `/api/badge` endpoint to get the
   * annotation count for a URI.
   */

  /**
   * TabState stores the Hypothesis-related state for tabs in the current browser
   * session.
   */
  class TabState {
    /**
     * Current Hypothesis-related state for each tab.
     *
     * @param onchange - Callback invoked when state for a tab changes
     */
    constructor(onchange) {
      /** Map of URI to annotation count. */
      _defineProperty(this, "_annotationCountCache", void 0);
      /** Map of tab ID to current badge request. */
      _defineProperty(this, "_pendingAnnotationCountRequests", void 0);
      _defineProperty(this, "_currentState", void 0);
      /** Callback invoked when a tab's state changes. */
      _defineProperty(this, "onchange", void 0);
      this._currentState = new Map();
      this._pendingAnnotationCountRequests = new Map();
      this._annotationCountCache = new Map();
      this.onchange = onchange;
    }

    /**
     * Mark a tab as having Hypothesis loaded in it.
     */
    activateTab(tabId) {
      this.setState(tabId, {
        state: 'active'
      });
    }

    /**
     * Mark a tab as not having Hypothesis loaded.
     */
    deactivateTab(tabId) {
      this.setState(tabId, {
        state: 'inactive'
      });
    }

    /**
     * Mark a tab as having encountered an error when trying to load Hypothesis into it.
     */
    errorTab(tabId, error) {
      this.setState(tabId, {
        state: 'errored',
        error
      });
    }

    /**
     * Remove Hypothesis-related state about a given tab.
     */
    clearTab(tabId) {
      this.setState(tabId, null);
      this._pendingAnnotationCountRequests.delete(tabId);
    }

    /**
     * Return the current Hypothesis-related state for a tab.
     */
    getState(tabId) {
      return this._currentState.get(tabId) ?? DEFAULT_STATE;
    }

    /**
     * Return the badge count for a given tab.
     */
    annotationCount(tabId) {
      return this.getState(tabId).annotationCount;
    }

    /**
     * Return `true` if Hypothesis is loaded into a given tab.
     */
    isTabActive(tabId) {
      return this.getState(tabId).state === 'active';
    }

    /**
     * Return `true` if Hypothesis is not loaded in a given tab.
     */
    isTabInactive(tabId) {
      return this.getState(tabId).state === 'inactive';
    }

    /**
     * Return `true` if an error occurred while trying to load Hypothesis into a given tab.
     */
    isTabErrored(tabId) {
      return this.getState(tabId).state === 'errored';
    }

    /**
     * Updates the H state for a tab.
     *
     * @param tabId - The ID of the tab being updated
     */
    setState(tabId, stateUpdate) {
      let newState;
      if (stateUpdate) {
        newState = Object.assign({}, this.getState(tabId), stateUpdate);
        if (newState.state !== 'errored') {
          newState.error = undefined;
        }
      }
      if (isShallowEqual(newState, this._currentState.get(tabId))) {
        return;
      }
      if (newState) {
        this._currentState.set(tabId, newState);
      } else {
        this._currentState.delete(tabId);
      }
      if (this.onchange) {
        this.onchange(tabId, newState);
      }
    }

    /**
     * Request the current annotation count for the tab's URL.
     *
     * @param tabId The id of the tab.
     * @param tabUrl The URL of the tab.
     */
    async updateAnnotationCount(tabId, tabUrl) {
      const INITIAL_WAIT_MS = 1000;
      const MAX_WAIT_MS = 3000;
      const CACHE_EXPIRATION_MS = 3000;
      const pendingRequest = this._pendingAnnotationCountRequests.get(tabId);
      let url;
      try {
        url = uriForBadgeRequest(tabUrl);
      } catch {
        return;
      }
      const annotationCount = this._annotationCountCache.get(url);
      if (annotationCount !== undefined) {
        this.setState(tabId, {
          annotationCount
        });
        return;
      }
      const wait = Math.min((pendingRequest === null || pendingRequest === void 0 ? void 0 : pendingRequest.waitMs) ?? INITIAL_WAIT_MS, MAX_WAIT_MS);
      pendingRequest === null || pendingRequest === void 0 ? void 0 : pendingRequest.cancel();
      const debouncedFetch = new Promise((resolve, reject) => {
        const timerId = setTimeout(async () => {
          let count = this._annotationCountCache.get(url);
          if (count !== undefined) {
            resolve(count);
            return;
          }
          try {
            count = await fetchAnnotationCount(url);
            this._annotationCountCache.set(url, count);
            setTimeout(() => this._annotationCountCache.delete(url), CACHE_EXPIRATION_MS);
          } catch {
            count = 0;
          }
          this._pendingAnnotationCountRequests.delete(tabId);
          resolve(count);
        }, wait);
        this._pendingAnnotationCountRequests.set(tabId, {
          cancel: () => {
            clearTimeout(timerId);
            reject(new RequestCanceledError('Badge request canceled'));
          },
          waitMs: wait * 2
        });
      });
      try {
        const annotationCount = await debouncedFetch;
        this.setState(tabId, {
          annotationCount
        });
      } catch (error) {
        if (error instanceof RequestCanceledError) {
          // Do nothing
          return;
        }
        throw error;
      }
    }
  }

  /** Options for {@link Extension.activate}. */

  /**
   * Normalize a URL for comparison. This strips the fragment and converts
   * `http://` to `https://`.
   */
  function normalizeURL(url) {
    try {
      const parsed = new URL(url);
      parsed.hash = '';
      if (parsed.protocol === 'http:') {
        parsed.protocol = 'https:';
      }
      return parsed.toString();
    } catch {
      return url;
    }
  }

  /**
   * Return true if two URLs are equal, ignoring the fragment and treating HTTP
   * and HTTPS as equivalent.
   */
  function urlsEqual(urlA, urlB) {
    return normalizeURL(urlA) === normalizeURL(urlB);
  }

  /**
   * The main extension background application.
   *
   * This is responsible for tracking the state of the extension in each tab and
   * injecting of the client when the extension is activated by clicking the
   * extension's toolbar icon.
   *
   * Initializing the extension has two steps:
   *
   *  1. Create an instance of `Extension`
   *  2. Call the async {@link Extension.init} method to initialize the
   *     extension state for existing tabs.
   */
  class Extension {
    constructor() {
      _defineProperty(this, "firstRun", void 0);
      _defineProperty(this, "activate", void 0);
      _defineProperty(this, "init", void 0);
      _defineProperty(this, "_onTabStateChange", void 0);
      const help = new HelpPage();
      const state = new TabState(onTabStateChange);
      const browserAction = new BrowserAction();
      const sidebarInjector = new SidebarInjector();
      const currentlyLoadingUrl = new Map(); // keeps tracks of what URL each tab is loading

      /**
       * Pending activations of extension. This is a map of tab ID to activation
       * URL and options. The activation is applied when the tab navigates to
       * the given URL.
       */
      const pendingActivations = new Map();

      /**
       * Opens the onboarding page.
       */
      this.firstRun = async extensionInfo => {
        // If we've been installed because of an administrative policy, then don't
        // open the welcome page in a new tab.
        //
        // It's safe to assume that if an admin policy is responsible for installing
        // the extension, opening the welcome page is going to do more harm than
        // good, as it will appear that a tab opened without user action.
        //
        // See:
        //
        //   https://developer.chrome.com/extensions/management#type-ExtensionInstallType
        //
        if (extensionInfo.installType === 'admin') {
          return;
        }
        const tab = await chromeAPI.tabs.create({
          url: settings.serviceUrl + 'welcome'
        });
        state.activateTab(tab.id);
      };

      /**
       * Activate the extension on a specific tab.
       */
      this.activate = (tabId, options = {}) => {
        if (options.afterNavigationTo) {
          pendingActivations.set(tabId, options);
        } else {
          state.setState(tabId, {
            state: 'active',
            directLinkQuery: directLinkQuery(options.query ?? '') ?? undefined
          });
        }
      };

      /**
       * Initialize cached state for browser tabs by querying each tab to see
       * whether the extension is active there.
       *
       * This should be called when the extension is loaded or reloaded.
       */
      const initTabStates = async () => {
        const tabs = await chromeAPI.tabs.query({});
        const activeStates = await Promise.all(tabs.map(async tab => {
          if (tab.id === undefined) {
            return false;
          }
          try {
            const active = await sidebarInjector.isClientActiveInTab(tab);
            return active;
          } catch (e) {
            console.warn(`Unable to determine extension state in tab ${tab.id}`, e);
            return false;
          }
        }));
        for (let i = 0; i < tabs.length; i++) {
          const tab = tabs[i];
          if (!tab.id) {
            continue;
          }
          const isActive = activeStates[i];

          // nb. If tab status is not available, we optimistically assume it is
          // loaded.
          const ready = tab.status === 'complete' || typeof tab.status !== 'string';
          state.setState(tab.id, {
            state: isActive ? 'active' : 'inactive',
            extensionSidebarInstalled: isActive,
            ready
          });
        }
      };
      async function onTabStateChange(tabId, current) {
        if (!current) {
          return;
        }
        let tab;
        try {
          tab = await chromeAPI.tabs.get(tabId);
        } catch {
          state.clearTab(tabId);
          return;
        }
        browserAction.update(tabId, current);
        addOrRemoveClientFromTab(tab);
      }

      // exposed for use by tests
      this._onTabStateChange = onTabStateChange;
      async function onBrowserActionClicked(tab) {
        const tabId = tab.id;
        const tabError = state.getState(tabId).error;
        if (tabError) {
          help.showHelpForError(tab, tabError);
        } else if (state.isTabActive(tabId)) {
          state.deactivateTab(tabId);
        } else {
          // Immediately request additional permissions we may need for this
          // specific tab, before any async calls. See notes in
          // `requestExtraPermissionsForTab` docs.
          //
          // eslint-disable-next-line no-lonely-if
          if (await sidebarInjector.requestExtraPermissionsForTab(tab)) {
            state.activateTab(tabId);
          } else {
            state.errorTab(tabId, new Error('Hypothesis could not get the permissions needed to load in this tab'));
          }
        }
      }

      /**
       * Returns the active state for a tab which has just been navigated to.
       */
      function activeStateForNavigatedTab(tabId) {
        let activeState = state.getState(tabId).state;
        if (activeState === 'errored') {
          // user had tried to activate H on the previous page but it failed,
          // retry on the new page
          activeState = 'active';
        }
        return activeState;
      }
      function resetTabState(tabId, url) {
        state.setState(tabId, {
          state: activeStateForNavigatedTab(tabId),
          ready: false,
          annotationCount: 0,
          extensionSidebarInstalled: false
        });
        updateAnnotationCountIfEnabled(tabId, url);
      }

      /**
       * This function will be called multiple times as the tab reloads.
       * https://developer.chrome.com/extensions/tabs#event-onUpdated
       *
       * 'changeInfo' contains details of what changed about the tab's status.
       * Two important events are when the tab's `status` changes to `loading`
       * when the user begins a new navigation and when the tab's status changes
       * to `complete` after the user completes a navigation
       */
      const onTabUpdated = (tabId, {
        status
      }, tab) => {
        // `url` property is included because manifest has the `tabs` permission
        const url = tab.url;
        const loadingUrl = currentlyLoadingUrl.get(tabId);
        if (status === 'loading' && url !== loadingUrl) {
          currentlyLoadingUrl.set(tabId, url);
          resetTabState(tabId, url);
          const query = directLinkQuery(url);
          if (query) {
            state.setState(tabId, {
              directLinkQuery: query
            });
          }
        } else if (status === 'complete') {
          currentlyLoadingUrl.delete(tabId);
          const tabState = state.getState(tabId);
          let newActiveState = tabState.state;
          if (tabState.directLinkQuery) {
            newActiveState = 'active';
          }
          state.setState(tabId, {
            ready: true,
            state: newActiveState
          });
        }

        // Apply activations scheduled for when tab navigates to its current URL.
        //
        // We compare normalized URLs because the browser may modify the fragment
        // or redirect HTTP to HTTPS, compared to the URL we expected the tab
        // to navigate to.
        const pendingActivation = pendingActivations.get(tabId);
        if (pendingActivation !== null && pendingActivation !== void 0 && pendingActivation.afterNavigationTo && urlsEqual(pendingActivation.afterNavigationTo, url)) {
          pendingActivations.delete(tabId);

          // Clear the URL so that the activation takes effect immediately.
          pendingActivation.afterNavigationTo = undefined;
          this.activate(tabId, pendingActivation);
        }
      };
      async function onTabReplaced(addedTabId, removedTabId) {
        state.setState(addedTabId, {
          state: activeStateForNavigatedTab(removedTabId),
          ready: true
        });
        state.clearTab(removedTabId);
        const tab = await chromeAPI.tabs.get(addedTabId);
        updateAnnotationCountIfEnabled(addedTabId, tab.url);
      }
      function onTabCreated(tab) {
        // Clear the state in case there is old, conflicting data in storage.
        if (tab.id) {
          onTabRemoved(tab.id);
        }
      }
      function onTabRemoved(tabId) {
        currentlyLoadingUrl.delete(tabId);
        state.clearTab(tabId);
      }

      /**
       * Load or unload the Hypothesis client in a tab to match the corresponding
       * state in {@link state}.
       */
      async function addOrRemoveClientFromTab(tab) {
        const tabId = tab.id;

        // If the tab has not yet finished loading then just quietly return.
        if (!state.getState(tabId).ready) {
          return;
        }
        const isInstalled = state.getState(tabId).extensionSidebarInstalled;
        if (state.isTabActive(tabId) && !isInstalled) {
          // Optimistically set the state flag indicating that the sidebar has
          // been installed.
          state.setState(tabId, {
            extensionSidebarInstalled: true
          });
          const {
            directLinkQuery
          } = state.getState(tabId);

          // Configure client to load assets from extension.
          //
          // Note this configuration is duplicated in `pdfjs-init.js`. Any changes
          // made here must be reflected there as well.
          const config = {
            assetRoot: chromeAPI.runtime.getURL('/client/'),
            notebookAppUrl: chromeAPI.runtime.getURL('/client/notebook.html'),
            profileAppUrl: chromeAPI.runtime.getURL('/client/profile.html'),
            sidebarAppUrl: chromeAPI.runtime.getURL('/client/app.html'),
            // Pass the direct-link query as configuration into the client.
            //
            // The reason we don't rely on just putting this into the URL and letting
            // the client pick it up is to make direct-linking work in sites/apps
            // that modify the URL fragment as they load. See commit 3143ca27e05d.
            ...directLinkQuery
          };
          try {
            await sidebarInjector.injectIntoTab(tab, config);

            // Clear the direct link once H has been successfully injected.
            state.setState(tabId, {
              directLinkQuery: undefined
            });
          } catch (err) {
            if (err instanceof AlreadyInjectedError) {
              state.setState(tabId, {
                state: 'inactive',
                extensionSidebarInstalled: false
              });
              return;
            }
            if (!shouldIgnoreInjectionError(err)) {
              report(err, 'Injecting Hypothesis sidebar', {
                url: tab.url
              });
            }
            state.errorTab(tabId, err);
          }
        } else if (state.isTabInactive(tabId) && isInstalled) {
          await sidebarInjector.removeFromTab(tab);
          state.setState(tabId, {
            extensionSidebarInstalled: false
          });
        }
      }
      async function updateAnnotationCountIfEnabled(tabId, url) {
        // If user disabled the badge count, this call to `sync.get` will
        // return `{ badge: false}`
        const {
          badge
        } = await chromeAPI.storage.sync.get({
          badge: true // the default value `true` is returned only if `badge` is not yet set.
        });

        if (badge) {
          state.updateAnnotationCount(tabId, url);
        }
      }

      /**
       * Initialize the extension.
       *
       * This queries the state of the extension in existing tabs and sets up
       * event listeners to respond to future tab changes.
       *
       * If the extension's state in a particular tab cannot be determined,
       * the extension is assumed not to be loaded in that tab.
       *
       * @return - A promise that resolves once listeners have been set up and
       *   the state of existing tabs has been determined.
       */
      this.init = async () => {
        chromeAPI.browserAction.onClicked.addListener(onBrowserActionClicked);

        // Set up listeners for tab events.
        chromeAPI.tabs.onCreated.addListener(onTabCreated);

        // When a user navigates within an existing tab, onUpdated is fired in most cases
        chromeAPI.tabs.onUpdated.addListener(onTabUpdated);

        // ... but when a user navigates to a page that is loaded
        // via prerendering or instant results, onTabReplaced is
        // fired instead. See https://developer.chrome.com/extensions/tabs#event-onReplaced
        // and https://code.google.com/p/chromium/issues/detail?id=109557
        chromeAPI.tabs.onReplaced.addListener(onTabReplaced);
        chromeAPI.tabs.onRemoved.addListener(onTabRemoved);

        // Determine the state of the extension in existing tabs.
        await initTabStates();
      };
    }
  }

  /**
   * Initialize the extension's Service Worker / background page.
   *
   * This is exported for use in tests.
   */
  async function init() {
    var _chromeAPI$runtime$re, _chromeAPI$runtime;
    const extension = new Extension();
    const initialized = extension.init();

    // Tokens indicating which features the current extension supports.
    const allFeatures = [
    // "activate" message to activate extension on current tab and
    // optionally first navigate to a different URL.
    'activate'];
    chromeAPI.runtime.onInstalled.addListener(async installDetails => {
      // Check whether this is the inital installation or an update of an existing
      // installation.
      if (installDetails.reason === 'install') {
        const extensionInfo = await chromeAPI.management.getSelf();
        extension.firstRun(extensionInfo);
      }
    });

    // Respond to messages sent by the JavaScript from https://hyp.is.
    // This is how it knows whether the user has this Chrome extension installed.
    chromeAPI.runtime.onMessageExternal.addListener((request, sender, sendResponse) => {
      switch (request.type) {
        case 'ping':
          {
            const queryFeatures = request.queryFeatures ?? [];
            const features = allFeatures.filter(f => queryFeatures.includes(f));
            sendResponse({
              type: 'pong',
              features
            });
          }
          break;
        case 'activate':
          {
            var _sender$tab;
            if (typeof ((_sender$tab = sender.tab) === null || _sender$tab === void 0 ? void 0 : _sender$tab.id) !== 'number') {
              return;
            }
            const {
              url,
              query
            } = request;
            if (url) {
              chromeAPI.tabs.update(sender.tab.id, {
                url
              });
            }
            extension.activate(sender.tab.id, {
              afterNavigationTo: url,
              query
            });
            sendResponse({
              active: true
            });
          }
          break;
      }
    });
    (_chromeAPI$runtime$re = (_chromeAPI$runtime = chromeAPI.runtime).requestUpdateCheck) === null || _chromeAPI$runtime$re === void 0 ? void 0 : _chromeAPI$runtime$re.call(_chromeAPI$runtime).then(() => {
      chromeAPI.runtime.onUpdateAvailable.addListener(() => chromeAPI.runtime.reload());
    });
    await initialized;
  }

  // nb. We use `globalThis` for the global object because it is `window` in Karma
  // tests but `self` in the real extension's Service Worker.
  const inTests = ('__karma__' in globalThis);
  if (!inTests) {
    init();
  }

  exports.init = init;

  return exports;

})({});
