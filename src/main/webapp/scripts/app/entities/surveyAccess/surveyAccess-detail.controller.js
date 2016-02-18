'use strict';

angular.module('patientfocusApp')
    .controller('SurveyAccessDetailController', function ($scope, $rootScope, $stateParams, entity, SurveyAccess) {
        $scope.surveyAccess = entity;
        $scope.load = function (id) {
            SurveyAccess.get({id: id}, function (result) {
                $scope.surveyAccess = result;
            });
        };
        $rootScope.$on('patientfocusApp:surveyAccessUpdate', function (event, result) {
            $scope.surveyAccess = result;
        });
    });
