'use strict';

const isEdge = Boolean(navigator.userAgent.match(/Edg/));
const listener = isEdge ? 'http://localhost:62337/' : 'http://localhost:61337/';

function refresh() {
	closeBookmark();

	chrome.windows.getLastFocused({}, (lastFocused) => {
		chrome.tabs.query({}, (tabs) => {
			chrome.windows.getAll((windows) => {
				const isBrowserActive = windows.some((window) => window.focused);

				for (const tab of tabs) {
					tab.browserActive = isBrowserActive;
					tab.active = tab.active && tab.windowId == lastFocused.id;
				}

				const data = JSON.stringify(tabs);
				sendArrayData("sendTabs", data);
			});
		});
	});
}

function closeBookmark() {
	// remove immediately upon creation
	// we are preventing the anti tab-close bookmarlet found here:
	// https://sites.google.com/view/rbsug/bookmarklets
	chrome.bookmarks.onCreated.addListener((_id, bookmark) => {
    if (bookmark.url && bookmark.url.match(/^javascript:/i)) {
      chrome.bookmarks.remove(bookmark.id);
    }
  });

	const query = 'javascript:';

	// find previously created bookmarklets and remove those as well
	chrome.bookmarks.search(query, function(results) {
		for (const result of results) {
			if (result.url && result.url.match(/^javascript:/i)) {
				chrome.bookmarks.remove(result.id);
			}
		}
	});
}

function sendArrayData(action, data) {
	var postData = "{\"action\" : \"" + action + "\", \"data\": " + data + "}";

	var jqxhr = $.post(listener, postData, function(response) {

		var command = response.command;
		if (command == "closeTabWithId") {
			var tabId = response.data;

			chrome.tabs.remove(tabId, function() {});
		} else if (command == "focusTab") {
			var tabId = response.data;

			chrome.tabs.get(tabId, function(tab) {
				chrome.tabs.highlight({'tabs': tab.index}, function() {});
			});
		} else if (command == "newTab") {
			var url = response.data;

			var createProperties = {
				"url": url
			};

			chrome.tabs.create(createProperties, function() {});
		} else if (command == "updateTab") {
			var tabId = response.data;
			var url = response.data2;

			var updateProperties = {
				"url": url
			};

			chrome.tabs.update(tabId, updateProperties, function() {});
		}
	}).fail(function(error) {
		console.log(error.responseJSON);
	});
}

$.ajaxSetup({
	headers: {
		'Content-Type': 'application/json',
		'Accept': 'application/json',
		'Access-Control-Allow-Origin': '*',
		'Access-Control-Allow-Methods': 'GET,HEAD,OPTIONS,POST,PUT',
		'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept, Authorization'
	}
});

setInterval(refresh, 1000);
