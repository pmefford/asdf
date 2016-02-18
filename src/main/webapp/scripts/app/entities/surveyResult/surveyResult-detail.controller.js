'use strict';

angular.module('patientfocusApp')
    .controller('SurveyResultDetailController', function ($scope, $rootScope, $stateParams, entity, SurveyResult) {
        $scope.surveyResult = entity;
        $scope.load = function (id) {
            SurveyResult.get({id: id}, function (result) {
                $scope.surveyResult = result;
            });
        };
        $rootScope.$on('patientfocusApp:surveyResultUpdate', function (event, result) {
            $scope.surveyResult = result;
        });
    });
