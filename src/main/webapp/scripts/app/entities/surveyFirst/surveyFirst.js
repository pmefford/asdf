'use strict';

angular.module('patientfocusApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('surveyFirst', {
                parent: 'entity',
                url: '/surveyFirsts',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'SurveyFirsts'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/surveyFirst/surveyFirsts.html',
                        controller: 'SurveyFirstController'
                    }
                },
                resolve: {}
            })
            .state('surveyFirst.detail', {
                parent: 'entity',
                url: '/surveyFirst/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'SurveyFirst'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/surveyFirst/surveyFirst-detail.html',
                        controller: 'SurveyFirstDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'SurveyFirst', function ($stateParams, SurveyFirst) {
                        return SurveyFirst.get({id: $stateParams.id});
                    }]
                }
            })
            .state('surveyFirst.new', {
                parent: 'surveyFirst',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/surveyFirst/surveyFirst-dialog.html',
                        controller: 'SurveyFirstDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {field1: null, field2: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('surveyFirst', null, {reload: true});
                        }, function () {
                            $state.go('surveyFirst');
                        })
                }]
            })
            .state('surveyFirst.edit', {
                parent: 'surveyFirst',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/surveyFirst/surveyFirst-dialog.html',
                        controller: 'SurveyFirstDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SurveyFirst', function (SurveyFirst) {
                                return SurveyFirst.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('surveyFirst', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
