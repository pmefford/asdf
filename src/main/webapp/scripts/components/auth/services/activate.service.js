'use strict';

angular.module('patientfocusApp')
    .factory('Activate', function ($resource) {
        return $resource('api/activate', {}, {
            'get': {method: 'GET', params: {}, isArray: false}
        });
    });


