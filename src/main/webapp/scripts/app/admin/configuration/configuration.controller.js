'use strict';

angular.module('patientfocusApp')
    .controller('ConfigurationController', function ($scope, ConfigurationService) {
        ConfigurationService.get().then(function (configuration) {
            $scope.configuration = configuration;
        });
    });
