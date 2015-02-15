var _                  = require('lodash'),
		Promise						 = require('bluebird'),

		Logger						 = require('../util/logger.js'),

		FormattingManager,
		FormattingUtility;

FormattingManager = (function () {
	
	var activeFormatters = {};

	return {
		
		/**
		 * Sets a new method to handle a formatting event
		 *
		 * @param {string} event The event tag for the method
		 * @param {function} method The function to pass upon event call; should contain the arguments ({string}, {Messenger}, {Channel})
		 * 
		 */
		on: function (event, method) {
			if (_.isNull(event) || _.isUndefined(event) || !_.isString(event)) {
				Logger.warn('Attempted to register formatting event with no event tag');
				return;	
			}

			if (_.isNull(event) || _.isUndefined(event) || !_.isFunction(method)) {
				Logger.warn('Attempted to register formatting method with non-function');
				return;	
			}

			if (!_.has(activeFormatters, event)) {
				activeFormatters[event] = [];
				activeFormatters[event].push(method);
			} else {
				if (_.includes(activeFormatters[event], method)) {
					Logger.warn('Attempted to insert duplicate formatting event method');
				} else {
					activeFormatters[event].push(method);
				}
			}
		},

		/**
		 * Calls the formatting event sequence for an asynchronous formatting sequence
		 *
		 * @param {string} object.event The event name to activate for channel formatting
		 * @param {string} object.message The message to format
		 * @param {Messenger} object.messenger The messenger sending the message
		 * @param {Channel} object.targetChannel The desired target channel
		 * @return {Promise(string)} 
		 */
		call: function (object) {
			if (_.isEmpty(object)) {
				return Promise.reject(new Error('Empty formatting call'));
			}

			var editedMessage = object.message;

			for (var method in activeFormatters[object.event]) {
				var methodRes = method(object.message, object.messenger, object.targetChannel);

				if (!_.isString(methodRes)) {
					return Promise.reject(new TypeError('Non-string return for formatter method: ' + method.toString()));
				} else {
					editedMessage = methodRes;
				}
			}

			return Promise.resolve(editedMessage);
		}
	};

}());

FormattingUtility = (function () {
	var COLOUR_PATTERN = /&[A-Fa-fK-Ok-orR0-9]/g;

	return {
		colourise: function(input) {
			if (_.isNull(input) || !_.isString(input)) {
				Logger.debug('Tried to colourise a non-string: ' + input);
				return;
			}

			var colourised = input,
					array      = colourised.match(COLOUR_PATTERN);

			for (var str in array) {
				colourised = colourised.replace(str, str.replace('&', '§'));
			}

			return colourised;
		}
	};
}());


module.exports.manager = FormattingManager;
module.exports.util    = FormattingUtility;