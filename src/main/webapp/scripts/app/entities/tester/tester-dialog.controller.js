'use strict';

angular.module('patientfocusApp').controller('TesterDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Tester',
        function ($scope, $stateParams, $modalInstance, entity, Tester) {

            $scope.tester = entity;
            $scope.load = function (id) {
                Tester.get({id: id}, function (result) {
                    $scope.tester = result;
                });
            };

            var onSaveFinished = function (result) {
                console.log('emit result', result);
                $scope.$emit('patientfocusApp:testerUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.tester.id != null) {
                    Tester.update($scope.tester, onSaveFinished);
                } else {
                    Tester.save($scope.tester, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
