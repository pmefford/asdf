'use strict';

angular.module('patientfocusApp').controller('SurveyResultDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SurveyResult',
        function ($scope, $stateParams, $modalInstance, entity, SurveyResult) {

            $scope.surveyResult = entity;
            $scope.load = function (id) {
                SurveyResult.get({id: id}, function (result) {
                    $scope.surveyResult = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('patientfocusApp:surveyResultUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.surveyResult.id != null) {
                    SurveyResult.update($scope.surveyResult, onSaveFinished);
                } else {
                    SurveyResult.save($scope.surveyResult, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
