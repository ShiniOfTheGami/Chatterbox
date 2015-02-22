var _      = require ('lodash');

var extend = function (proto, statics, strong) {
  var _proto  = proto || {},
      _static = statics || {},
      _strong = strong || false,
      parent  = this,
      child;

  if (_.has(_proto, 'constructor')) {
    child = _proto.constructor;
  } else {
    child = function () { parent.apply(this, arguments); };
  }

  _.extend(child, parent, statics);

  var prime = function () { this.constructor = child; };
  prime.prototype = parent.prototype;
  child.prototype = new prime();

  _.extend(child.prototype, _proto);

  if (_strong) {
    child.__super = parent.prototype;
  }

  return child;
};

// for storing data
var Model = function () {

};

_.assign(Model.prototype, {
  add: function (key, entry) {
    // Adds an array entry to an existing array
    // - Will verify the array's existence
    // @return this
  },

  remove: function (key, entry) {
    // Removes an array entry from an existing array

  },

  set: function (object) {
    // Set an object in the model;
    // - Will verify object's keys against a 'serialised' object in the statics
    // @return this
  },

  unset: function (object) {
    // Unset an object in the model;
    // - Will verify the object's existence before removing the entry
    // - Will verify the object's keys against an 'locked' object in the statics
    // @return this
  },

  toJSON: function () {
    // Serialise the model to JSON
    // @return {string}
  }
});

_.assign(Model, {
  // Type ID identifier
  typeId: 'default',

  serialised: ['typeId']

  deserialise: function (json) {
    // Deserialses a model from a JSON object
    // - Verifies against the {obj}.typeId to make sure the deserialising function is deserialising the correct model
    // @return {Model}
  },

  search: function (key, object) {
    // Find a certain serialised key in the object
  }
});

// For storing and serialising an array of models
var Collection = function (type, models) {
  var col    = this._col    = models || [],
      typeId = this._typeId = type;

};

_.assign(Collection.prototype, {
  verify = function (object) {
    // Verifies that the input object subscribes to the proper type
  },

  add = function (model) {
    // Add a verified model to the collection
  }
});

Collection.extend = Model.extend = extend;

module.exports.Model      = Model;
module.exports.Collection = Collection;