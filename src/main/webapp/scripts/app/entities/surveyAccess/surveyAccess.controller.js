'use strict';

angular.module('patientfocusApp')
    .controller('SurveyAccessController', function ($scope, SurveyAccess, SurveyContent, ParseLinks) {
        $scope.surveyAccess = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            SurveyAccess.query({page: $scope.page, per_page: 20}, function (result, headers) {
                //$scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.surveyAccess.push(result[i]);
                }
            });
        };
        $scope.reset = function () {
            $scope.page = 1;
            $scope.surveyAccess = [];
            $scope.loadAll();
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SurveyAccess.get({id: id}, function (result) {
                $scope.surveyAccess = result;
                $('#deleteSurveyAccessConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SurveyAccess.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteSurveyAccessConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.surveyAccess = {field1: null, field2: null, id: null};
        };
    });
