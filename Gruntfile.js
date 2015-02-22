var Grunt =  function (grunt) {
  
  // Configuration section
  var config = {
    pkg: grunt.file.readJSON('package.json')
  };

  grunt.initConfig(config);


};

module.exports = Grunt;