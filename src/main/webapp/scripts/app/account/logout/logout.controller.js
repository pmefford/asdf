'use strict';

angular.module('patientfocusApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
