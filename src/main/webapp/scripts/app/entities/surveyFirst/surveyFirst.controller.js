'use strict';

angular.module('patientfocusApp')
    .controller('SurveyFirstController', function ($scope, SurveyFirst, ParseLinks) {
        $scope.surveyFirsts = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            SurveyFirst.query({page: $scope.page, per_page: 20}, function (result, headers) {
                //$scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.surveyFirsts.push(result[i]);
                }
            });
        };
        $scope.reset = function () {
            $scope.page = 1;
            $scope.surveyFirsts = [];
            $scope.loadAll();
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SurveyFirst.get({id: id}, function (result) {
                $scope.surveyFirst = result;
                $('#deleteSurveyFirstConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SurveyFirst.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteSurveyFirstConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.surveyFirst = {field1: null, field2: null, id: null};
        };
    });
