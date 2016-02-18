'use strict';

angular.module('patientfocusApp')
    .controller('TesterDetailController', function ($scope, $rootScope, $stateParams, entity, Tester) {
        $scope.tester = entity;
        $scope.load = function (id) {
            Tester.get({id: id}, function (result) {
                $scope.tester = result;
            });
        };
        $rootScope.$on('patientfocusApp:testerUpdate', function (event, result) {
            console.log('catch emitted result', result, event);
            $scope.tester = result;
        });
    });
