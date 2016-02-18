'use strict';

angular.module('patientfocusApp')
    .controller('TesterController', function ($scope, Tester, ParseLinks) {
        $scope.testers = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            Tester.query({page: $scope.page, per_page: 20}, function (result, headers) {
                //$scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.testers.push(result[i]);
                }
            });
        };
        $scope.reset = function () {
            $scope.page = 1;
            $scope.testers = [];
            $scope.loadAll();
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Tester.get({id: id}, function (result) {
                $scope.tester = result;
                $('#deleteTesterConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Tester.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteTesterConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.tester = {field1: null, field2: null, id: null};
        };
    });
