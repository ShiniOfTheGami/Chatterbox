var _ = require('lodash');

module.exports = function (proto, statics) {
  var _proto  = proto || {},
      _static = statics || {}
      parent  = this,
      child;

  if (_.has(_proto, 'constructor')) {
    child = _proto.constructor;
  } else {
    child = function() { parent.apply(this, arguments); };
  }

  _.extend(child, parent, statics);

  var prime = function() { this.constructor = child; };
  prime.prototype = parent.prototype;
  child.prototype = new prime();

  _.extend(child.prototype, _proto);

  child.__super = parent.prototype;

  return child;
};