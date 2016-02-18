'use strict';

angular.module('patientfocusApp')
    .factory('SurveyResult', function ($resource, DateUtils) {
        return $resource('api/surveyResult/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    });
