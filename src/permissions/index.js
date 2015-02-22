var _                  = require('lodash'),
    Promise            = require('bluebird'),
    Logger             = require("../util/logger.js"),
    PermissionsManager;

PermissionsManager = (function () {
  var self    = this;

    return{

       has: function(object){
            if(_.isEmpty(object)|| !_.isArray(object)){
                Logger.warn("Attempted to check parse permissions context");
                return Promise.reject(new Error("Empty context"));
            }
           if(_.isEmpty(object.requester)||_.isEmpty(object.action)|| _.isEmpty(object.target)){
               return Promise.reject(new Error("Missing Arguments in context"));
           }

           var user         = object.requester;
           var userperms    = user.permissions;
           if(_.has(userperms,object.action)){
               var actionperms      = _.values(userperms[object.action]);
               if(_.has(actionperms, object.target)){
                   return Promise.resolve("Is Allowed");
               }
           }
           var usergroups = user.groups;
           if(_.isEmpty(usergroups)|| !_.isArray(usergroups)){
               return Promise.reject(new Error("No group memberships defined"));
           }

           _.forEach(usergroups, function(n){
               var group;
               //group = convert(UUID TO GROUP); TODO
               if(_.has(group.permissions,object.action)){
                   var groupactionperms      = _.values(group.permissions[object.action]);
                   if(_.has(groupactionperms, object.target)){
                       return Promise.resolve("Is Allowed");
                   }
               }
           });

           return Promise.reject("User is not allowed to perform this action");

       }
    }
}());
