'use strict';

angular.module('patientfocusApp')
    .controller('SurveyContentDetailController', function ($scope, $rootScope, $stateParams, entity, SurveyContent) {
        $scope.surveyContent = entity;

        $rootScope.$on('patientfocusApp:surveyContentUpdate', function (event, result) {
            $scope.surveyContent = result;
        });
    });
