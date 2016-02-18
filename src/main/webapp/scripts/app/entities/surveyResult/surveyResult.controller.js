'use strict';

angular.module('patientfocusApp')
    .controller('SurveyResultController', function ($scope, SurveyResult, ParseLinks) {
        $scope.surveyResults = [];
        $scope.page = 1;
        $scope.category = '';
        $scope.loadAll = function () {
            SurveyResult.query({page: $scope.page, per_page: 20}, function (result, headers) {
                //$scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.surveyResults.push(result[i]);
                }
            });
        };
        $scope.reset = function () {
            $scope.page = 1;
            $scope.surveyResults = [];
            $scope.loadAll();
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SurveyResult.get({id: id}, function (result) {
                $scope.surveyResult = result;
                $('#deleteSurveyResultConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SurveyResult.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteSurveyResultConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.surveyResult = {field1: null, field2: null, id: null};
        };
    });
