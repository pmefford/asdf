'use strict';

angular.module('patientfocusApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('surveyContent', {
                parent: 'entity',
                url: '/surveyContent',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'SurveyContent'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/surveyContent/surveyContent.html',
                        controller: 'SurveyContentController'
                    }
                },
                resolve: {}
            })
            .state('surveyContent.detail', {
                parent: 'entity',
                url: '/surveyContent/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'SurveyContent'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/surveyContent/surveyContent-detail.html',
                        controller: 'SurveyContentDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'SurveyContent', function ($stateParams, SurveyContent) {
                        return SurveyContent.get({id: $stateParams.id});
                    }]
                }
            })
            .state('surveyContent.new', {
                parent: 'surveyContent',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/surveyContent/surveyContent-dialog.html',
                        controller: 'SurveyContentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    questions: [{text: [{locale: 'en_US', fullText: ''}], key: 'q0'}],
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('surveyContent', null, {reload: true});
                        }, function () {
                            $state.go('surveyContent');
                        })
                }]
            })
            .state('surveyContent.edit', {
                parent: 'surveyContent',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/surveyContent/surveyContent-dialog.html',
                        controller: 'SurveyContentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SurveyContent', function (SurveyContent) {
                                return SurveyContent.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('surveyContent', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
