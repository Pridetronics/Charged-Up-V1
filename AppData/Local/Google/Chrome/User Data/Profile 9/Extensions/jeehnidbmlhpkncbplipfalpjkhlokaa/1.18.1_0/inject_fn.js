// @ts-nocheck
function loadJSInPage(path, name, cb) {
  var script = document.createElement("script");

  script.onload = function(){
    if(cb) {
      cb();
    }
  };

  script.onerror = function() {
    console.error(name + ": Failed for frame " + window.location.href);
  };

  script.src = chrome.runtime.getURL(path);
  console.log("inserting script from: " + script.src);

  document.head.appendChild(script);
}
