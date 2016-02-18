'use strict';

angular.module('patientfocusApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {});
    });


