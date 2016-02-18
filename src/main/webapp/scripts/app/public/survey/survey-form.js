'use strict';

angular.module('patientfocusApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('survey-form', {
                parent: 'site',
                url: '/survey/{locale}/{id}?uid&occasion',
                data: {
                    roles: [],
                    pageTitle: 'Questions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/public/survey/survey-form.html',
                        controller: 'SurveyFormController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PublicSurvey', function ($stateParams, PublicSurvey) {
                        return PublicSurvey.questions({id: $stateParams.id});
                    }]
                }
            })
            .state('survey-form.demographics', {
                parent: 'site',
                url: '/demographics/{id}',
                data: {
                    roles: [],
                    pageTitle: 'Demographics'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/public/survey/demographics-form.html',
                        controller: 'DemographicFormController'
                    }
                },
                resolve: {
                    surveyQuestions: ['$stateParams', 'PublicSurvey', function ($stateParams, PublicSurvey) {
                        console.log('demo resolve', $stateParams);
                        var result = PublicSurvey.reportId({id: $stateParams.id});
                        console.log('result', result);
                        return result;
                    }]
                }
            })
            .state('survey-form.finish', {
                parent: 'site',
                url: '/thankyou',
                data: {
                    roles: [],
                    pageTitle: 'Thank You'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/public/survey/survey-form-thanks.html',
                        controller: 'SurveyFormController'
                    }
                }
            });
    });
