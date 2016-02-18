'use strict';

angular.module('patientfocusApp')
    .factory('LogsService', function ($resource) {
        return $resource('api/logs', {}, {
            'findAll': {method: 'GET', isArray: true},
            'changeLevel': {method: 'PUT'}
        });
    });
