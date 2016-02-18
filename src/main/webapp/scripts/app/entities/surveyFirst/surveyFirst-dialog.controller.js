'use strict';

angular.module('patientfocusApp').controller('SurveyFirstDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SurveyFirst',
        function ($scope, $stateParams, $modalInstance, entity, SurveyFirst) {

            $scope.surveyFirst = entity;
            $scope.load = function (id) {
                SurveyFirst.get({id: id}, function (result) {
                    $scope.surveyFirst = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('patientfocusApp:surveyFirstUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.surveyFirst.id != null) {
                    SurveyFirst.update($scope.surveyFirst, onSaveFinished);
                } else {
                    SurveyFirst.save($scope.surveyFirst, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
