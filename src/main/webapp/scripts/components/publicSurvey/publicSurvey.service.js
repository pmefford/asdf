'use strict';

angular.module('patientfocusApp')
    .factory('PublicSurvey', function ($resource, DateUtils) {
        return $resource('', {}, {
            'questions': {
                url: 'api/public/questions/:id',
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': {
                url: 'api/public/save',
                method: 'POST'
            },
            'reportId': {
                url: 'api/public/load/:id',
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'report': {
                url: 'api/public/report/:id',
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
