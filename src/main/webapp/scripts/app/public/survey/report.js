'use strict';

angular.module('patientfocusApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('survey-report', {
                parent: 'site',
                url: '/report/{id}',
                data: {
                    roles: [],
                    pageTitle: 'Report'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/public/survey/survey-form-report.html',
                        controller: 'SurveyFormReportController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PublicSurvey', function ($stateParams, PublicSurvey) {
                        console.log('loadreport', $stateParams);
                        return PublicSurvey.report({id: $stateParams.id});
                    }]
                }
            });
    });
