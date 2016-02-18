'use strict';

angular.module('patientfocusApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('surveyAccess', {
                parent: 'entity',
                url: '/surveyAccess',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'SurveyAccess'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/surveyAccess/surveyAccess.html',
                        controller: 'SurveyAccessController'
                    }
                },
                resolve: {}
            })
            .state('surveyAccess.detail', {
                parent: 'entity',
                url: '/surveyAccess/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'SurveyAccess'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/surveyAccess/surveyAccess-detail.html',
                        controller: 'SurveyAccessDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'SurveyAccess', function ($stateParams, SurveyAccess) {
                        return SurveyAccess.get({id: $stateParams.id});
                    }]
                }
            })
            .state('surveyAccess.new', {
                parent: 'surveyAccess',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/surveyAccess/surveyAccess-dialog.html',
                        controller: 'SurveyAccessDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {surveyName: null, source: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('surveyAccess', null, {reload: true});
                        }, function () {
                            $state.go('surveyAccess');
                        })
                }]
            })
            .state('surveyAccess.edit', {
                parent: 'surveyAccess',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/surveyAccess/surveyAccess-dialog.html',
                        controller: 'SurveyAccessDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SurveyAccess', function (SurveyAccess) {
                                return SurveyAccess.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('surveyAccess', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
