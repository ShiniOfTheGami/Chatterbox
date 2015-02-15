var clc = require ('cli-clc'),

    Logger,
    Colors;

Colors = {
  info : clc.green,
  warn : clc.yellow,
  error : clc.red.bold,
  debug : clc.white
};

Logger = function (_prefix, _activated) {
  var prefix         = _prefix || '',
      debugActivated = _activated || false;

  return {
    debug: function (message) {
      if (debugActivated) {
        console.log(prefix + ': ' + Colors.debug(message));
      }
    },

    info: function (message) {
      console.log(this.prefix + ': ' + Colors.info(message));
    },

    error: function (message) {
      console.log(this.prefix + ': ' + Colors.error(message));
    },

    warn: function (message) {
      console.log(this.prefix + ': ' + Colors.warn(message));
    }
  };
};

module.exports = Logger;