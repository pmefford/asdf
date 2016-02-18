'use strict';

angular.module('patientfocusApp').controller('SurveyAccessDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SurveyAccess', 'SurveyContent',
        function ($scope, $stateParams, $modalInstance, entity, SurveyAccess, SurveyContent) {
            $scope.surveyContents;
            $scope.surveyAccess = entity;
            $scope.load = function (id) {
                SurveyAccess.get({id: id}, function (result) {
                    $scope.surveyAccess = result;
                });
            };

            $scope.initScope = function () {
                SurveyContent.query(function (results) {
                    console.log(results)
                    $scope.surveyContents = results;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('patientfocusApp:surveyAccessUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.surveyAccess.id != null) {
                    SurveyAccess.update($scope.surveyAccess, onSaveFinished);
                } else {
                    SurveyAccess.save($scope.surveyAccess, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
            $scope.chooseSurvey = function (name) {
                $scope.surveyAccess.surveyName = name;
            }
        }]);
