'use strict';

angular.module('patientfocusApp')
    .controller('SurveyFirstDetailController', function ($scope, $rootScope, $stateParams, entity, SurveyFirst) {
        $scope.surveyFirst = entity;
        $scope.load = function (id) {
            SurveyFirst.get({id: id}, function (result) {
                $scope.surveyFirst = result;
            });
        };
        $rootScope.$on('patientfocusApp:surveyFirstUpdate', function (event, result) {
            $scope.surveyFirst = result;
        });
    });
