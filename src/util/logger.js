var chalk = require ('chalk'),
    Logger,
    Colors;

Colors = {
  info : chalk.green,
  warn : chalk.yellow,
  error : chalk.red.bold,
  debug : chalk.white
};

Logger = function (_prefix, _activated) {
  var self           =  this,
      prefix         = self.prefix = _prefix || '',
      debugActivated = self.debugActivated = _activated || false;

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