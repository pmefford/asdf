'use strict';

angular.module('patientfocusApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tester', {
                parent: 'entity',
                url: '/testers',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Testers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tester/testers.html',
                        controller: 'TesterController'
                    }
                },
                resolve: {}
            })
            .state('tester.detail', {
                parent: 'entity',
                url: '/tester/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Tester'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tester/tester-detail.html',
                        controller: 'TesterDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Tester', function ($stateParams, Tester) {
                        return Tester.get({id: $stateParams.id});
                    }]
                }
            })
            .state('tester.new', {
                parent: 'tester',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tester/tester-dialog.html',
                        controller: 'TesterDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {field1: null, field2: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('tester', null, {reload: true});
                        }, function () {
                            $state.go('tester');
                        })
                }]
            })
            .state('tester.edit', {
                parent: 'tester',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tester/tester-dialog.html',
                        controller: 'TesterDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Tester', function (Tester) {
                                return Tester.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('tester', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
