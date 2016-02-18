'use strict';

angular.module('patientfocusApp')
    .controller('SurveyContentController', function ($scope, SurveyContent, ParseLinks) {
        $scope.surveyContents = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            SurveyContent.query({page: $scope.page, per_page: 20}, function (result, headers) {
                //$scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.surveyContents.push(result[i]);
                }
            });
        };
        $scope.reset = function () {
            $scope.page = 1;
            $scope.surveyContents = [];
            $scope.loadAll();
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SurveyContent.get({id: id}, function (result) {
                $scope.surveyContent = result;
                $('#deleteSurveyContentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SurveyContent.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteSurveyContentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.surveyContent = {field1: null, field2: null, id: null};
        };
    });
