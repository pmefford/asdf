'use strict';

angular.module('patientfocusApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('surveyResult', {
                parent: 'entity',
                url: '/surveyResult',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'SurveyResults'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/surveyResult/surveyResults.html',
                        controller: 'SurveyResultController'
                    }
                },
                resolve: {}
            })

            .state('surveyResult.detail', {
                parent: 'entity',
                url: '/surveyResult/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Tester'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/surveyResult/surveyResult-detail.html',
                        controller: 'SurveyResultDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'SurveyResult', function ($stateParams, SurveyResult) {
                        return SurveyResult.get({id: $stateParams.id});
                    }]
                }
            })
        ;
    });
