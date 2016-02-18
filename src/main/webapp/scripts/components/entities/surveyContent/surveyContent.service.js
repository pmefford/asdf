'use strict';

angular.module('patientfocusApp')
    .factory('SurveyContent', function ($resource, DateUtils) {
        return $resource('api/surveyContent/:id', {}, {
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
